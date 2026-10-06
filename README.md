# aamoza vpn

**ساخته شده توسط [t.me/aamoza](https://t.me/aamoza)**

کلاینت VPN چندموتوره برای اندروید با UI مدرن (Jetpack Compose + Material 3).

| موتور | توضیح |
|--------|--------|
| **WARP** | آماده برای هسته Aether (MASQUE + WireGuard) + توکن Cloudflare |
| **Worker** | آماده برای Xray / sing-box (VLESS / Trojan) |

> این نسخه اسکلت کامل UI + سرویس VPN + مدیریت دو موتور را دارد. برای اتصال واقعی باید فایل‌های native (`.so`) اضافه شوند.

---

## ساده‌ترین راه بیلد (پیشنهادی)

1. پروژه را از گیت‌هاب دانلود یا Clone کنید
2. با **Android Studio** باز کنید (Ladybug یا جدیدتر)
3. اگر Gradle Wrapper خواست، گزینه Generate / Sync را بزنید
4. از منو: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. فایل APK در مسیر `app/build/outputs/apk/` ساخته می‌شود

نیازی به دستور خط فرمان یا نصب جداگانه Gradle نیست.

---

## بیلد با GitHub Actions

هر پوش روی `main` به‌صورت خودکار APK می‌سازد. از تب **Actions** فایل را از Artifacts دانلود کنید.

---

## امضای Release (کاهش هشدار «برنامه خطرناک»)

```bash
keytool -genkeypair -v -keystore aamoza.keystore -alias aamoza -keyalg RSA -keysize 2048 -validity 10950
```

فایل `keystore.properties` بسازید:

```properties
storeFile=aamoza.keystore
storePassword=پسورد_شما
keyAlias=aamoza
keyPassword=پسورد_شما
```

سپس در Android Studio بیلد Release بگیرید. این فایل‌ها را داخل گیت نگذارید.

---

## هسته Native

محل قرارگیری:

```
app/src/main/jniLibs/arm64-v8a/libaamoza.so
app/src/main/jniLibs/armeabi-v7a/libaamoza.so
```

بدون این فایل‌ها اپ نصب و اجرا می‌شود، اما اتصال واقعی برقرار نمی‌شود.

---

## مشخصات

- پکیج: `com.aamoza.vpn`
- minSdk 26 / targetSdk 35
- Kotlin + Compose + Material 3
- VpnService رسمی اندروید
- ProGuard برای Release فعال

ساخته شده توسط **t.me/aamoza**
