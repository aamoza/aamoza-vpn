package com.aamoza.vpn.engine

interface VpnEngine {
    val mode: EngineMode

    /**
     * @param tunFd  فایل‌دیسکریپتور اینترفیس تونل (فقط برای عبور به native؛ close نکنید)
     * @param protect برای protect کردن سوکت‌های موتور روی اینترفیس فیزیکی
     */
    suspend fun start(tunFd: Int, config: EngineConfig, protect: (Int) -> Boolean): Result<String>

    fun stop()
}
