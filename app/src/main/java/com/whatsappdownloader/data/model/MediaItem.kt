package com.whatsappdownloader.data.model

import java.io.File

data class MediaItem(
    val file: File,
    val name: String,
    val path: String,
    val size: Long,
    val type: MediaType,
    val dateModified: Long
) {
    val extension: String
        get() = file.extension.lowercase()
    
    val isImage: Boolean
        get() = type == MediaType.IMAGE
    
    val isVideo: Boolean
        get() = type == MediaType.VIDEO
    
    val isDocument: Boolean
        get() = type == MediaType.DOCUMENT
    
    val isAudio: Boolean
        get() = type == MediaType.AUDIO
}

enum class MediaType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    UNKNOWN
}
