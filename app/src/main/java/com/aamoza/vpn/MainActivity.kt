package com.aamoza.vpn

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aamoza.vpn.service.AamozaVpnService
import com.aamoza.vpn.ui.AamozaApp
import com.aamoza.vpn.ui.theme.AamozaTheme

class MainActivity : ComponentActivity() {

    private var pendingStart by mutableStateOf(false)

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && pendingStart) {
            startVpnService()
        }
        pendingStart = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AamozaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AamozaApp(
                        onConnectRequest = { requestVpnPermissionAndStart() },
                        onDisconnectRequest = { stopVpnService() }
                    )
                }
            }
        }
    }

    private fun requestVpnPermissionAndStart() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            pendingStart = true
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpnService()
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, AamozaVpnService::class.java).apply {
            action = AamozaVpnService.ACTION_CONNECT
        }
        startForegroundService(intent)
    }

    private fun stopVpnService() {
        val intent = Intent(this, AamozaVpnService::class.java).apply {
            action = AamozaVpnService.ACTION_DISCONNECT
        }
        startService(intent)
    }
}
