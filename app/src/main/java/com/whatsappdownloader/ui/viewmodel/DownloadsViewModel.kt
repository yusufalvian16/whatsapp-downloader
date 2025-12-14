package com.whatsappdownloader.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.whatsappdownloader.utils.DownloadNotifier
import com.whatsappdownloader.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class DownloadsViewModel(application: Application) : AndroidViewModel(application) {

    private val fileUtils = FileUtils(application)

    private val _downloadedFiles = MutableStateFlow<List<File>>(emptyList())
    val downloadedFiles: StateFlow<List<File>> = _downloadedFiles.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadDownloads()
        
        DownloadNotifier.downloadCompleted
            .onEach { loadDownloads() }
            .launchIn(viewModelScope)
    }

    fun loadDownloads() {
        viewModelScope.launch {
            _isLoading.value = true
            val files = fileUtils.getDownloadedMedia()
            _downloadedFiles.value = files
            _isLoading.value = false
        }
    }

    fun deleteFile(file: File) {
        viewModelScope.launch {
            if (fileUtils.deleteDownloadedFile(file)) {
                loadDownloads()
            }
        }
    }
}
