package am.testapp.presentation.view_models

import am.testapp.App
import am.testapp.models.FileType
import am.testapp.models.MediaListModel
import am.testapp.shared.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daasuu.mp4compose.FillMode
import com.daasuu.mp4compose.composer.Mp4Composer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


class MediaSharedViewModel : ViewModel() {

    private val mutableFinishCreateFileLiveData = MutableLiveData<Unit>()
    val finishCreateFileLiveData: LiveData<Unit> = mutableFinishCreateFileLiveData

    private val mutableErrorUploadLiveData = MutableLiveData<Unit>()
    val errorUploadLiveData: LiveData<Unit> = mutableErrorUploadLiveData

    private val mutableSuccessUploadLiveData = MutableLiveData<Unit>()
    val successUploadLiveData: LiveData<Unit> = mutableSuccessUploadLiveData

    private val mutableGetUrisLiveData = MutableLiveData<List<Uri>>()
    val getUrisLiveData: LiveData<List<Uri>> = mutableGetUrisLiveData

    var mediaListModel = ArrayList<MediaListModel>()

    fun getUri(uris: List<Uri>) {
        mutableGetUrisLiveData.value = uris
    }

    fun getMedia(uri: List<Uri>) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                uri.forEach {
                    checkFileType(it)
                }
            }

            mutableFinishCreateFileLiveData.value = Unit
        }
    }

    private suspend fun checkFileType(uri: Uri) = withContext(Dispatchers.IO) {
        var path: String? = null
        var fileType: FileType? = null
        when {
            uri.isVideo() -> {
                path = compressVideo(uri.getVideoPath())
                fileType = FileType.VIDEO
            }
            uri.isImage() -> {
                path = compressImage(uri)
                fileType = FileType.IMAGE
            }
        }

        val thumbnail = createThumbnails(path, fileType)

        val file = path?.let { File(it) }
        val fileLength = file?.length()?.toDouble()
        val size: Double? = (fileLength?.div(((1024 * 1024))))

        mediaListModel.add(
            MediaListModel().copy(
                name = file?.name,
                fileType = fileType,
                size = size,
                path = path,
                thumbnailPath = thumbnail
            )
        )
    }


    private suspend fun createThumbnails(
        path: String?, type: FileType?
    ): Bitmap? = withContext(Dispatchers.IO) {

        val widthHeight = (70).toPx.toInt()
        val bitmapThumbnail = if (type == FileType.VIDEO) {
            createThumbnail(path)
        } else {
            ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path),
                widthHeight, widthHeight
            )
        }
        return@withContext bitmapThumbnail
    }


    private fun compressVideo(imagePath: String?): String {

        val sourceFile = imagePath?.let { File(it) }
        val targetLocation =
            App.instance
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?.absolutePath + "/" + sourceFile?.name


        imagePath?.let {
            Mp4Composer(it, targetLocation)
                .size(1280, 720)
                .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                .listener(object : Mp4Composer.Listener {
                    override fun onProgress(progress: Double) {
                        Log.d("TAG", "onProgress = $progress")
                    }

                    override fun onCurrentWrittenVideoTime(timeUs: Long) {
                    }

                    override fun onCompleted() {

                    }

                    override fun onCanceled() {
                    }

                    override fun onFailed(exception: Exception) {
                    }
                })
                .start()
        }
        return targetLocation
    }

    private suspend fun compressImage(path: Uri?): String? {
        val sourceFile = path?.getImagePath()?.let { File(it) }
        val sourceFileName = sourceFile?.name

        return path?.toBitmap()?.changeFile(sourceFileName)
    }


    private suspend fun Bitmap.changeFile(name: String?): String = withContext(Dispatchers.IO) {
        val targetLocation =
            App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath

        val f = File(targetLocation, "/${name}")
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val bitmapData = getCompressedBitmapData(this@changeFile)


        val fos: FileOutputStream?
        try {
            fos = FileOutputStream(f)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return@withContext f.absolutePath
    }

    private suspend fun getCompressedBitmapData(bitmap: Bitmap): ByteArray =
        withContext(Dispatchers.IO) {
            var quality = 100
            var bitmapData: ByteArray
            bitmapData = getByteArray(bitmap, quality)
            while (bitmapData.size > 1.5 * 1024 * 1024) {
                quality -= 3
                bitmapData = getByteArray(bitmap, quality)
            }
            return@withContext bitmapData
        }

    private fun getByteArray(bitmap: Bitmap, quality: Int): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            quality,
            bos
        )
        return bos.toByteArray()
    }

    private suspend fun createThumbnail(path: String?): Bitmap? = withContext(Dispatchers.IO) {
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        var bitmap: Bitmap? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(App.instance, Uri.parse(path))
            bitmap = mediaMetadataRetriever.getFrameAtTime(
                10,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever?.release()
        }
        return@withContext bitmap
    }

    fun uploadMedia() {
        val storage: FirebaseStorage = Firebase.storage
        val model = mediaListModel
        val storageRef = storage.reference
        repeat(model.size) {
            val mountainsRef = model[it].name?.let { it1 -> storageRef.child(it1) }

            val byteArray = model[it].path?.let { it1 -> File(it1).readBytes() }

            val uploadTask = byteArray?.let { it1 -> mountainsRef?.putBytes(it1) }
            uploadTask?.addOnFailureListener {
                mutableErrorUploadLiveData.value = Unit
            }?.addOnSuccessListener { _ ->
                if (it == 4) {
                    mutableSuccessUploadLiveData.value = Unit
                }
            }
        }
    }
}