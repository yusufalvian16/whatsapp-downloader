package com.whatsappdownloader.data.repository

import android.content.Context
import com.whatsappdownloader.data.model.MediaItem
import com.whatsappdownloader.data.model.MediaType
import com.whatsappdownloader.utils.WhatsAppPathHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

class MediaRepository(private val context: Context) {
    
    private val whatsAppPathHelper = WhatsAppPathHelper(context)
    
    fun scanWhatsAppStatus(): Flow<List<MediaItem>> = flow {
        val mediaList = withContext(Dispatchers.IO) {
            val statusPath = whatsAppPathHelper.getStatusPath()
            if (statusPath == null || !statusPath.exists()) {
                emptyList()
            } else {
                scanDirectory(statusPath)
            }
        }
        emit(mediaList)
    }
    
    private fun scanDirectory(directory: File): List<MediaItem> {
        if (!directory.exists() || !directory.isDirectory) {
            return emptyList()
        }
        
        val files = directory.listFiles() ?: return emptyList()
        
        return files
            .filter { it.isFile && it.canRead() }
            .mapNotNull { file ->
                val mediaType = getMediaType(file.extension)
                if (mediaType != MediaType.UNKNOWN) {
                    MediaItem(
                        file = file,
                        name = file.name,
                        path = file.absolutePath,
                        size = file.length(),
                        type = mediaType,
                        dateModified = file.lastModified()
                    )
                } else {
                    null
                }
            }
            .sortedByDescending { it.dateModified }
    }
    
    private fun getMediaType(extension: String): MediaType {
        val ext = extension.lowercase()
        return when (ext) {
            "jpg", "jpeg", "png", "gif", "webp", "bmp" -> MediaType.IMAGE
            "mp4", "3gp", "mkv", "webm", "avi", "mov" -> MediaType.VIDEO
            "mp3", "wav", "ogg", "m4a", "aac", "amr" -> MediaType.AUDIO
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "zip", "rar" -> MediaType.DOCUMENT
            else -> MediaType.UNKNOWN
        }
    }
}
