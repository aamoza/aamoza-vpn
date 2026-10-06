package com.aamoza.vpn.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * موتور Worker — آماده برای Xray-core یا sing-box.
 */
class WorkerEngine : VpnEngine {

    override val mode: EngineMode = EngineMode.WORKER

    private external fun workerStart(fd: Int, configJson: String): Int
    private external fun workerStop()

    @Volatile
    private var running = false

    override suspend fun start(
        tunFd: Int,
        config: EngineConfig,
        protect: (Int) -> Boolean,
    ): Result<String> = withContext(Dispatchers.IO) {
        if (!NativeLibLoader.loaded) {
            return@withContext Result.failure(
                VpnException("هسته Native برای Worker یافت نشد — libaamoza.so را در jniLibs قرار دهید")
            )
        }
        if (config.address.isBlank()) {
            return@withContext Result.failure(VpnException("آدرس Worker خالی است"))
        }

        val json = JSONObject()
            .put("mode", "worker")
            .put("server", config.address)
            .put("id", config.token)
            .put("protocol", "vless")
            .put("transport", "ws")
            .put("cleanIp", true)
            .put("antiDpi", true)
            .put("mtu", 1280)
            .toString()

        val code = workerStart(tunFd, json)
        if (code == 0) {
            running = true
            Result.success("Worker")
        } else {
            Result.failure(VpnException("اتصال Worker ناموفق بود (کد $code)"))
        }
    }

    override fun stop() {
        if (running) runCatching { workerStop() }
        running = false
    }
}
