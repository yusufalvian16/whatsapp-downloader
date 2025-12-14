# WhatsApp Downloader

Aplikasi Android ringan dan cepat untuk mengunduh media dari WhatsApp Status. Aplikasi ini dirancang untuk tidak berjalan di background dan hanya aktif saat digunakan.

## Fitur

- ✅ Unduh media dari WhatsApp Status (Gambar, Video, Audio, Dokumen)
- ✅ UI modern dengan Jetpack Compose dan Material Design 3
- ✅ Ringan dan cepat - tidak berjalan di background
- ✅ Preview media sebelum download
- ✅ Manajemen file unduhan
- ✅ Support Android 7.0+ (API 24+)

## Teknologi

- **Kotlin** - Bahasa pemrograman modern
- **Jetpack Compose** - UI framework modern
- **Material Design 3** - Desain UI konsisten
- **Coroutines** - Async operations yang efisien
- **Coil** - Image loading library yang ringan

## Build Requirements

- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17
- Android SDK 34
- Gradle 8.2+

## Cara Build & Install ke HP

### Opsi 1: Langsung dari Android Studio (Paling Mudah)
1. Buka project di Android Studio
2. Hubungkan HP ke komputer dengan USB (aktifkan USB Debugging)
3. Klik "Run" atau tekan `Shift+F10`
4. Aplikasi akan otomatis terinstall dan terbuka di HP

### Opsi 2: Build APK dan Install Manual
1. Build APK: **Build > Build Bundle(s) / APK(s) > Build APK(s)**
2. APK ada di: `app/build/outputs/apk/debug/app-debug.apk`
3. Transfer APK ke HP (USB/Bluetooth/Email)
4. Install APK di HP (aktifkan "Install from Unknown Sources")

**📱 Lihat panduan lengkap di [CARA_INSTALL_KE_HP.md](CARA_INSTALL_KE_HP.md)**

## Cara Menggunakan

1. **Berikan Izin Penyimpanan**
   - Saat pertama kali membuka aplikasi, berikan izin akses penyimpanan
   - Untuk Android 11+, mungkin perlu memberikan izin melalui Settings

2. **Buat Status di WhatsApp**
   - Buka WhatsA    pp dan buat status dengan media (gambar/video)
   - Kembali ke aplikasi ini

3. **Unduh Media**
   - Pilih media yang ingin diunduh dari grid
   - Klik tombol "Unduh" di dialog
   - File akan tersimpan di folder `Downloads/WhatsAppDownloader`

4. **Lihat Unduhan**
   - Buka tab "Unduhan" di bottom navigation
   - Lihat semua file yang sudah diunduh
   - Hapus file jika diperlukan

## Struktur Project

```
app/
├── src/main/
│   ├── java/com/whatsappdownloader/
│   │   ├── MainActivity.kt
│   │   ├── data/
│   │   │   ├── model/MediaItem.kt
│   │   │   └── repository/MediaRepository.kt
│   │   ├── ui/
│   │   │   ├── screens/
│   │   │   ├── components/
│   │   │   └── theme/
│   │   ├── utils/
│   │   └── viewmodel/
│   └── res/
└── build.gradle.kts
```

## Catatan Penting

- Aplikasi ini **tidak berjalan di background** - semua operasi hanya terjadi saat aplikasi aktif
- Aplikasi memerlukan akses ke folder WhatsApp untuk membaca status
- File yang diunduh disimpan di folder Downloads/WhatsAppDownloader
- Aplikasi tidak mengirim data ke server manapun - semua proses lokal

## Permissions

Aplikasi memerlukan izin berikut:
- `READ_EXTERNAL_STORAGE` (Android 10 dan below)
- `READ_MEDIA_IMAGES`, `READ_MEDIA_VIDEO`, `READ_MEDIA_AUDIO` (Android 13+)
- `WRITE_EXTERNAL_STORAGE` (Android 10 dan below, untuk menyimpan file)

## Developer

**Dibuat oleh:** yusufalvian16

## License

Aplikasi ini dibuat untuk keperluan pribadi dan edukasi.
