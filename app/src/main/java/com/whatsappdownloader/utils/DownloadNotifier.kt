package com.whatsappdownloader.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DownloadNotifier {
    private val _downloadCompleted = MutableSharedFlow<Unit>(replay = 0)
    val downloadCompleted = _downloadCompleted.asSharedFlow()

    suspend fun notifyDownloadCompleted() {
        _downloadCompleted.emit(Unit)
    }
}
