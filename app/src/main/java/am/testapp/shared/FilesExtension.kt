package am.testapp.shared

import am.testapp.App
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.net.URLConnection


fun Uri.getImagePath(): String? {

    val wholeID = DocumentsContract.getDocumentId(this)
    val id = wholeID.split(":").toTypedArray()[1]

    val column = arrayOf(MediaStore.Images.Media.DATA)

    val sel = MediaStore.Images.Media._ID + "=?"

    val cursor: Cursor? = App.instance.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        column, sel, arrayOf(id), null
    )

    var filePath: String? = null

    cursor?.let {
        val columnIndex: Int = cursor.getColumnIndex(column[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }

        cursor.close()
    }
    return filePath
}

fun Uri.getVideoPath(): String? {
    val wholeID = DocumentsContract.getDocumentId(this)
    val id = wholeID.split(":").toTypedArray()[1]

    val column = arrayOf(MediaStore.Video.Media.DATA)

    val sel = MediaStore.Video.Media._ID + "=?"

    val cursor: Cursor? = App.instance.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        column, sel, arrayOf(id), null
    )

    var filePath: String? = null

    cursor?.let {
        val columnIndex: Int = cursor.getColumnIndex(column[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }

        cursor.close()
    }
    return filePath
}

fun Uri.isVideo(): Boolean {
    getVideoPath()?.let {
        val mimeType: String = URLConnection.guessContentTypeFromName(it)
        return mimeType.startsWith("video")
    }
    return false
}

fun Uri.isImage(): Boolean {
    getImagePath()?.let {
        val mimeType = URLConnection.guessContentTypeFromName(it)
        return mimeType != null && mimeType.startsWith("image")
    }
    return false
}

fun Uri.toBitmap(): Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    val source = ImageDecoder.createSource(App.instance.contentResolver, this)
    ImageDecoder.decodeBitmap(source)
} else MediaStore.Images.Media.getBitmap(App.instance.contentResolver, this)
