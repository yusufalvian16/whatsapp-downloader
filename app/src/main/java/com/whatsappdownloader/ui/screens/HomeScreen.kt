package com.whatsappdownloader.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whatsappdownloader.data.model.MediaItem
import com.whatsappdownloader.ui.components.MediaGrid
import com.whatsappdownloader.ui.viewmodel.HomeViewModel
import com.whatsappdownloader.ui.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onMediaClick: (MediaItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val downloadingItems by viewModel.downloadingItems.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(message) {
        message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WhatsApp Status") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadMedia() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    MediaGrid(
                        mediaItems = emptyList(),
                        onMediaClick = onMediaClick,
                        isLoading = true,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is HomeUiState.Empty -> {
                    MediaGrid(
                        mediaItems = emptyList(),
                        onMediaClick = onMediaClick,
                        isLoading = false,
                        emptyMessage = "Tidak ada status WhatsApp ditemukan.\nPastikan Anda memiliki status di WhatsApp.",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is HomeUiState.Success -> {
                    MediaGrid(
                        mediaItems = state.mediaList,
                        onMediaClick = onMediaClick,
                        isLoading = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
