# Instruksi Build WhatsApp Downloader

## Prasyarat

1. **Android Studio** - Hedgehog (2023.1.1) atau lebih baru
2. **JDK 17** - Pastikan JDK 17 terinstall
3. **Android SDK** - SDK Platform 34 dan Build Tools
4. **Gradle** - Akan di-download otomatis saat pertama kali build

## Langkah Build

### Menggunakan Android Studio

1. Buka Android Studio
2. Pilih **File > Open**
3. Pilih folder project `/media/yusuf/WORK/whatsapp`
4. Tunggu Gradle sync selesai (akan download dependencies otomatis)
5. Klik **Run** atau tekan `Shift+F10`

### Menggunakan Command Line

```bash
# Masuk ke folder project
cd /media/yusuf/WORK/whatsapp

# Build APK debug
./gradlew assembleDebug

# Build APK release (perlu keystore)
./gradlew assembleRelease

# Install ke device yang terhubung
./gradlew installDebug
```

APK akan berada di: `app/build/outputs/apk/debug/app-debug.apk`

## Troubleshooting

### Error: Gradle wrapper not found
Jika gradle-wrapper.jar tidak ada, jalankan:
```bash
./gradlew wrapper --gradle-version=8.2
```

### Error: SDK not found
1. Buka Android Studio
2. **Tools > SDK Manager**
3. Install **Android SDK Platform 34**
4. Install **Android SDK Build-Tools**

### Error: JDK not found
1. **File > Project Structure**
2. Pilih **SDK Location**
3. Set **JDK location** ke JDK 17

### Permission Denied untuk gradlew
```bash
chmod +x gradlew
```

## Testing

Aplikasi dapat diuji dengan:
1. Menghubungkan device Android atau emulator
2. Memastikan USB Debugging aktif
3. Klik **Run** di Android Studio

## Catatan

- Aplikasi memerlukan izin penyimpanan untuk berfungsi
- Pastikan WhatsApp sudah terinstall dan memiliki status
- File akan diunduh ke folder `Downloads/WhatsAppDownloader`
