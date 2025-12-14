package com.whatsappdownloader.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

class WhatsAppPathHelper(private val context: Context) {
    
    fun getStatusPath(): File? {
        val possiblePaths = getPossiblePaths()
        
        for (path in possiblePaths) {
            val file = File(path)
            if (file.exists() && file.isDirectory) {
                return file
            }
        }
        
        return null
    }
    
    private fun getPossiblePaths(): List<String> {
        val paths = mutableListOf<String>()
        
        // Primary storage path (most common)
        val primaryStorage = Environment.getExternalStorageDirectory().absolutePath
        paths.add("$primaryStorage/Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
        paths.add("$primaryStorage/WhatsApp/Media/.Statuses")
        
        // Alternative paths for different Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ scoped storage
            val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (mediaDir != null) {
                val parent = mediaDir.parentFile?.parentFile?.parentFile?.parentFile
                if (parent != null) {
                    paths.add("${parent.absolutePath}/Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
                }
            }
        }
        
        // Check for secondary storage
        val externalStorageDirs = context.getExternalFilesDirs(null)
        if (externalStorageDirs.size > 1) {
            val secondaryStorage = externalStorageDirs[1]
            val secondaryPath = secondaryStorage.absolutePath
            val basePath = secondaryPath.substringBefore("/Android/data")
            paths.add("$basePath/Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
            paths.add("$basePath/WhatsApp/Media/.Statuses")
        }
        
        return paths
    }
    
    fun getDownloadsPath(): File {
        val storyDir = File(Environment.getExternalStorageDirectory(), "WhatsappStory")
        if (!storyDir.exists()) {
            storyDir.mkdirs()
        }
        return storyDir
    }
}
