package am.testapp.models

import android.graphics.Bitmap

data class MediaListModel(
    val path: String? = null,
    var name: String? = null,
    var fileType: FileType? = null,
    var size: Double? = null,
    var thumbnailPath: Bitmap? = null
)

enum class FileType(val value: String) {
    VIDEO("video"),
    IMAGE("image")
}