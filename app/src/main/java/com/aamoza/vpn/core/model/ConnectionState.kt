package com.aamoza.vpn.core.model

import com.aamoza.vpn.engine.EngineMode

sealed interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connecting : ConnectionState
    data class Connected(val mode: EngineMode, val info: String) : ConnectionState
    data class Error(val message: String) : ConnectionState
}
