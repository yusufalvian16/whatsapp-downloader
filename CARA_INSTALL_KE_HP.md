# Cara Install Aplikasi ke HP Android

Panduan lengkap untuk menginstall aplikasi WhatsApp Downloader ke HP Android Anda.

## Metode 1: Menggunakan Android Studio (Paling Mudah)

### Persiapan:
1. **Install Android Studio** di komputer/laptop
2. **Hubungkan HP ke komputer** dengan kabel USB
3. **Aktifkan USB Debugging** di HP:
   - Buka **Settings** > **About Phone**
   - Ketuk **Build Number** sebanyak 7 kali (akan muncul "You are now a developer")
   - Kembali ke **Settings** > **Developer Options**
   - Aktifkan **USB Debugging**
   - Aktifkan **Install via USB** (jika ada)

### Langkah-langkah:

1. **Buka Project di Android Studio**
   ```
   File > Open > Pilih folder whatsapp
   ```

2. **Tunggu Gradle Sync** selesai (pertama kali akan download dependencies)

3. **Hubungkan HP ke Komputer**
   - Sambungkan HP dengan kabel USB
   - Di HP, akan muncul popup "Allow USB debugging?" → Pilih **Allow**

4. **Pilih Device di Android Studio**
   - Di toolbar atas, klik dropdown device
   - Pilih HP Anda (contoh: "Samsung Galaxy...")

5. **Klik Run atau tekan Shift+F10**
   - Aplikasi akan otomatis di-build dan install ke HP
   - Aplikasi akan langsung terbuka di HP

## Metode 2: Build APK dan Install Manual

### Langkah 1: Build APK

**Menggunakan Android Studio:**
1. Buka project di Android Studio
2. **Build > Build Bundle(s) / APK(s) > Build APK(s)**
3. Tunggu build selesai
4. Klik **locate** di notification yang muncul
5. APK ada di: `app/build/outputs/apk/debug/app-debug.apk`

**Menggunakan Command Line:**
```bash
cd /media/yusuf/WORK/whatsapp
./gradlew assembleDebug
```
APK akan ada di: `app/build/outputs/apk/debug/app-debug.apk`

### Langkah 2: Transfer APK ke HP

**Cara 1: Via USB**
1. Hubungkan HP ke komputer
2. Copy file `app-debug.apk` ke folder Downloads di HP
3. Lepas kabel USB

**Cara 2: Via Bluetooth/Email/Cloud**
1. Kirim APK via Bluetooth, Email, atau upload ke Google Drive
2. Download di HP

### Langkah 3: Install di HP

1. **Buka File Manager** di HP
2. **Cari file app-debug.apk** di folder Downloads
3. **Ketuk file APK**
4. **Aktifkan "Install from Unknown Sources"** jika diminta:
   - Settings > Security > Unknown Sources (aktifkan)
   - Atau Settings > Apps > Special Access > Install Unknown Apps
5. **Klik Install**
6. **Tunggu proses install selesai**
7. **Klik Open** atau cari aplikasi "WhatsApp Downloader" di app drawer

## Metode 3: Menggunakan ADB (Advanced)

### Install ADB:
- Windows: Download [Android Platform Tools](https://developer.android.com/studio/releases/platform-tools)
- Linux: `sudo apt install android-tools-adb`
- Mac: `brew install android-platform-tools`

### Langkah-langkah:

1. **Build APK** (lihat Metode 2)
2. **Hubungkan HP** dengan USB debugging aktif
3. **Install via ADB:**
   ```bash
   adb devices  # Cek apakah HP terdeteksi
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## Troubleshooting

### HP Tidak Terdeteksi di Android Studio

1. **Cek USB Debugging sudah aktif**
2. **Coba ganti kabel USB** (gunakan kabel data, bukan hanya charger)
3. **Install USB Driver** untuk HP Anda:
   - Samsung: Samsung USB Driver
   - Xiaomi: Mi USB Driver
   - Huawei: HiSuite
4. **Restart ADB:**
   ```bash
   adb kill-server
   adb start-server
   adb devices
   ```

### Error: "Installation failed"

1. **Uninstall versi lama** jika ada
2. **Cek storage HP** masih cukup
3. **Aktifkan "Install from Unknown Sources"**
4. **Coba install manual** dengan file manager

### Error: "App not installed"

1. **Cek signature** - pastikan uninstall versi lama dulu
2. **Cek Android version** - HP harus Android 7.0+ (API 24+)
3. **Cek storage** - pastikan masih ada ruang

### Aplikasi Crash saat Dibuka

1. **Cek logcat** di Android Studio untuk melihat error
2. **Pastikan izin penyimpanan** sudah diberikan
3. **Pastikan WhatsApp** sudah terinstall

## Tips

1. **Untuk testing cepat:** Gunakan Metode 1 (Android Studio)
2. **Untuk distribusi:** Gunakan Metode 2 (Build APK)
3. **Untuk development:** Gunakan Metode 1 dengan "Apply Changes" (hot reload)

## Setelah Install

1. **Buka aplikasi** WhatsApp Downloader
2. **Berikan izin penyimpanan** saat diminta
3. **Buka WhatsApp** dan buat status
4. **Kembali ke aplikasi** dan refresh
5. **Pilih media** yang ingin diunduh

## Catatan Penting

- ✅ HP harus Android 7.0 atau lebih baru
- ✅ USB Debugging harus aktif untuk metode 1 dan 3
- ✅ Install from Unknown Sources harus aktif untuk metode 2
- ✅ Pastikan WhatsApp sudah terinstall di HP
- ✅ Aplikasi memerlukan izin penyimpanan untuk berfungsi

---

**Developer:** yusufalvian16
