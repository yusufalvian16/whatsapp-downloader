package com.whatsappdownloader.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.whatsappdownloader.data.model.MediaItem
import com.whatsappdownloader.data.repository.MediaRepository
import com.whatsappdownloader.utils.DownloadNotifier
import com.whatsappdownloader.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = MediaRepository(application)
    private val fileUtils = FileUtils(application)
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private val _downloadingItems = MutableStateFlow<Set<String>>(emptySet())
    val downloadingItems: StateFlow<Set<String>> = _downloadingItems.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    init {
        loadMedia()
    }
    
    fun loadMedia() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.scanWhatsAppStatus().collect { mediaList ->
                if (mediaList.isEmpty()) {
                    _uiState.value = HomeUiState.Empty
                } else {
                    _uiState.value = HomeUiState.Success(mediaList)
                }
            }
        }
    }
    
    fun downloadMedia(mediaItem: MediaItem) {
        viewModelScope.launch {
            _downloadingItems.value = _downloadingItems.value + mediaItem.path
            
            val result = fileUtils.downloadMedia(mediaItem)
            
            _downloadingItems.value = _downloadingItems.value - mediaItem.path
            
            if (result.isSuccess) {
                _message.value = "Berhasil mengunduh: ${mediaItem.name}"
                DownloadNotifier.notifyDownloadCompleted()
            } else {
                _message.value = result.exceptionOrNull()?.message ?: "Gagal mengunduh"
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Empty : HomeUiState()
    data class Success(val mediaList: List<MediaItem>) : HomeUiState()
}
