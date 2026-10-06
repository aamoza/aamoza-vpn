package com.aamoza.vpn.engine

class EngineManager {

    private val engines: Map<EngineMode, VpnEngine> = mapOf(
        EngineMode.WARP to WarpEngine(),
        EngineMode.WORKER to WorkerEngine(),
    )

    @Volatile
    var active: VpnEngine? = null
        private set

    fun engineFor(mode: EngineMode): VpnEngine = engines.getValue(mode)

    suspend fun start(
        mode: EngineMode,
        tunFd: Int,
        config: EngineConfig,
        protect: (Int) -> Boolean,
    ): Result<String> {
        val engine = engineFor(mode)
        val result = engine.start(tunFd, config, protect)
        if (result.isSuccess) active = engine
        return result
    }

    fun stop() {
        active?.stop()
        active = null
    }
}
