package com.whatsappdownloader.utils

import android.content.Context
import android.media.MediaScannerConnection
import com.whatsappdownloader.data.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileUtils(private val context: Context) {
    
    private val whatsAppPathHelper = WhatsAppPathHelper(context)
    
    suspend fun downloadMedia(
        mediaItem: MediaItem,
        onProgress: ((Int) -> Unit)? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val sourceFile = mediaItem.file
            if (!sourceFile.exists() || !sourceFile.canRead()) {
                return@withContext Result.failure(Exception("Source file tidak dapat dibaca"))
            }
            
            val downloadsDir = whatsAppPathHelper.getDownloadsPath()
            val destinationFile = getUniqueFileName(downloadsDir, mediaItem.name)
            
            // Copy file with progress
            copyFileWithProgress(sourceFile, destinationFile, onProgress)

            // Notify the gallery
            MediaScannerConnection.scanFile(context, arrayOf(destinationFile.absolutePath), null, null)
            
            Result.success(destinationFile.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun downloadMultipleMedia(
        mediaItems: List<MediaItem>,
        onProgress: ((Int, Int) -> Unit)? = null
    ): List<Pair<MediaItem, Result<String>>> = withContext(Dispatchers.IO) {
        val results = mutableListOf<Pair<MediaItem, Result<String>>>()
        val total = mediaItems.size
        
        mediaItems.forEachIndexed { index, mediaItem ->
            val result = downloadMedia(mediaItem) { progress ->
                onProgress?.invoke(index, progress)
            }
            results.add(mediaItem to result)
        }
        
        results
    }
    
    private fun copyFileWithProgress(
        source: File,
        destination: File,
        onProgress: ((Int) -> Unit)?
    ) {
        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(8192)
                val totalBytes = source.length()
                var copiedBytes = 0L
                var bytesRead: Int
                
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    copiedBytes += bytesRead
                    
                    if (totalBytes > 0 && onProgress != null) {
                        val progress = ((copiedBytes * 100) / totalBytes).toInt()
                        onProgress(progress)
                    }
                }
            }
        }
    }
    
    private fun getUniqueFileName(directory: File, originalName: String): File {
        var file = File(directory, originalName)
        var counter = 1
        
        while (file.exists()) {
            val nameWithoutExt = originalName.substringBeforeLast(".")
            val extension = originalName.substringAfterLast(".", "")
            val newName = if (extension.isNotEmpty()) {
                "$nameWithoutExt($counter).$extension"
            } else {
                "$nameWithoutExt($counter)"
            }
            file = File(directory, newName)
            counter++
        }
        
        return file
    }
    
    fun getDownloadedMedia(): List<File> {
        val downloadsDir = whatsAppPathHelper.getDownloadsPath()
        if (!downloadsDir.exists() || !downloadsDir.isDirectory) {
            return emptyList()
        }
        
        return downloadsDir.listFiles()
            ?.filter { it.isFile }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }
    
    fun deleteDownloadedFile(file: File): Boolean {
        return try {
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
}
