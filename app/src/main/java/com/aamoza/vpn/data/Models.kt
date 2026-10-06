package com.aamoza.vpn.data

enum class EngineMode {
    WARP,
    WORKER
}

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

data class VpnConfig(
    val mode: EngineMode = EngineMode.WARP,
    val cloudflareToken: String = "",
    val workerUrl: String = "",
    val splitTunneling: Boolean = true
)
