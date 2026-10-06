package com.aamoza.vpn.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * موتور WARP — آماده برای هسته Aether (MASQUE + WireGuard).
 */
class WarpEngine : VpnEngine {

    override val mode: EngineMode = EngineMode.WARP

    private external fun warpStart(fd: Int, configJson: String): Int
    private external fun warpStop()

    @Volatile
    private var running = false

    override suspend fun start(
        tunFd: Int,
        config: EngineConfig,
        protect: (Int) -> Boolean,
    ): Result<String> = withContext(Dispatchers.IO) {
        if (!NativeLibLoader.loaded) {
            return@withContext Result.failure(
                VpnException("هسته Native برای WARP یافت نشد — libaamoza.so را در jniLibs قرار دهید")
            )
        }

        val json = JSONObject()
            .put("mode", "warp")
            .put("token", config.token)
            .put("masque", true)
            .put("wireguard", true)
            .put("mtu", 1280)
            .toString()

        val code = warpStart(tunFd, json)
        if (code == 0) {
            running = true
            Result.success("WARP")
        } else {
            Result.failure(VpnException("اتصال WARP ناموفق بود (کد $code)"))
        }
    }

    override fun stop() {
        if (running) runCatching { warpStop() }
        running = false
    }
}
