package com.whatsappdownloader

/**
 * WhatsApp Downloader Android App
 * 
 * Developer: yusufalvian16
 * Version: 1.0
 * 
 * Aplikasi ringan untuk mengunduh media dari WhatsApp Status
 */

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.whatsappdownloader.data.model.MediaItem
import com.whatsappdownloader.ui.screens.DownloadsScreen
import com.whatsappdownloader.ui.screens.HomeScreen
import com.whatsappdownloader.ui.screens.MediaDetailDialog
import com.whatsappdownloader.ui.screens.SettingsScreen
import com.whatsappdownloader.ui.theme.WhatsAppDownloaderTheme
import com.whatsappdownloader.ui.viewmodel.HomeViewModel
import com.whatsappdownloader.utils.PermissionHelper

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val showPermissionDialog = mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            homeViewModel.loadMedia()
        }
    }

    private val manageStorageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                homeViewModel.loadMedia()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhatsAppDownloaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WhatsAppDownloaderApp()

                    if (showPermissionDialog.value) {
                        PermissionRationaleDialog(
                            onConfirm = {
                                showPermissionDialog.value = false
                                launchManageStorageSettings()
                            },
                            onDismiss = {
                                showPermissionDialog.value = false
                            }
                        )
                    }
                }
            }
        }

        if (!PermissionHelper.hasStoragePermission(this)) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (!Environment.isExternalStorageManager()) {
                showPermissionDialog.value = true
                return
            }
        }

        val permissions = PermissionHelper.getRequiredPermissions()
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun launchManageStorageSettings() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            manageStorageResultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            manageStorageResultLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (PermissionHelper.hasStoragePermission(this)) {
            homeViewModel.loadMedia()
        }
    }
}

@Composable
fun WhatsAppDownloaderApp() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    var selectedMedia: MediaItem? by remember { mutableStateOf(null) }
    val downloadingItems by homeViewModel.downloadingItems.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda") },
                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Download, contentDescription = "Unduhan") },
                    label = { Text("Unduhan") },
                    selected = currentDestination?.hierarchy?.any { it.route == "downloads" } == true,
                    onClick = {
                        navController.navigate("downloads") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
                    label = { Text("Pengaturan") },
                    selected = currentDestination?.hierarchy?.any { it.route == "settings" } == true,
                    onClick = {
                        navController.navigate("settings") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = homeViewModel,
                    onMediaClick = { mediaItem ->
                        selectedMedia = mediaItem
                    }
                )
            }
            composable("downloads") {
                DownloadsScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }

    selectedMedia?.let { media ->
        val isDownloading = downloadingItems.contains(media.path)
        MediaDetailDialog(
            mediaItem = media,
            isDownloading = isDownloading,
            onDismiss = { selectedMedia = null },
            onDownload = {
                homeViewModel.downloadMedia(media)
                selectedMedia = null
            }
        )
    }
}

@Composable
fun PermissionRationaleDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Izin Diperlukan") },
        text = { Text("Aplikasi ini memerlukan izin akses semua file untuk dapat membaca status WhatsApp. Silakan aktifkan izin di pengaturan.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Buka Pengaturan")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
