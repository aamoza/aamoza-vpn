# aamoza vpn

**ساخته شده توسط [t.me/aamoza](https://t.me/aamoza)**

کلاینت اندروید مدرن با معماری دو موتوره (WARP + Worker).

> Dual-engine Android client — Cloudflare WARP (MASQUE / WireGuard) + improved Worker path.

---

## ویژگی‌ها

- موتور **WARP** با پشتیبانی از MASQUE و WireGuard
- موتور **Worker** با دامنه شخصی و Clean IP
- سوئیچ سریع بین دو مسیر
- رابط کاربری تمیز با Jetpack Compose + Material 3
- Split Tunneling آماده
- پشتیبانی از توکن Cloudflare
- برندینگ کامل: **aamoza vpn**

## وضعیت فعلی

این ریپازیتوری یک **پایه‌ی تمیز، مدرن و آماده توسعه** است:

- ساختار کامل Android (Kotlin + Compose)
- UI کامل با دو حالت WARP / Worker
- اسکلت VpnService
- GitHub Actions برای بیلد خودکار
- آماده برای اضافه کردن هسته‌های واقعی (Aether / Xray / sing-box)

## بیلد

```bash
git clone https://github.com/aamoza/aamoza-vpn.git
cd aamoza-vpn
./gradlew assembleDebug
```

APK در مسیر:
`app/build/outputs/apk/debug/app-debug.apk`

### GitHub Actions
هر پوش روی `main` به صورت خودکار Debug APK می‌سازد.

## ساختار

```
app/src/main/java/com/aamoza/vpn/
├── MainActivity.kt
├── ui/               # Compose screens & theme
├── service/          # VpnService
└── data/             # models & preferences
```

## لایسنس

MIT

---

**ساخته شده با دقت توسط t.me/aamoza**  
اگر مفید بود، ستاره بده ★
