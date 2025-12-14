package com.whatsappdownloader.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.whatsappdownloader.data.model.MediaItem
import com.whatsappdownloader.data.model.MediaType
import com.whatsappdownloader.ui.components.DownloadButton

@Composable
fun MediaDetailDialog(
    mediaItem: MediaItem?,
    isDownloading: Boolean,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    if (mediaItem == null) return
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = mediaItem.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (mediaItem.type) {
                    MediaType.IMAGE -> {
                        Image(
                            painter = rememberAsyncImagePainter(model = mediaItem.file),
                            contentDescription = mediaItem.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(300.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    MediaType.VIDEO -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = mediaItem.file),
                                contentDescription = mediaItem.name,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    else -> {
                        Text(
                            text = "Tipe: ${mediaItem.type.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ukuran:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatFileSize(mediaItem.size),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            DownloadButton(
                text = "Unduh",
                onClick = onDownload,
                isLoading = isDownloading,
                modifier = Modifier.padding(8.dp)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

private fun formatFileSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    
    return when {
        gb >= 1 -> String.format("%.2f GB", gb)
        mb >= 1 -> String.format("%.2f MB", mb)
        kb >= 1 -> String.format("%.2f KB", kb)
        else -> "$bytes B"
    }
}
