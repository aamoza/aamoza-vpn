package com.aamoza.vpn.engine

data class EngineConfig(
    val mode: EngineMode = EngineMode.WARP,
    val token: String = "",
    val address: String = "",
)
