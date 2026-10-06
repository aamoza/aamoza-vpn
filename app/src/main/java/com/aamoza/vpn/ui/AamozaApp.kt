package com.aamoza.vpn.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CloudQueue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aamoza.vpn.data.ConnectionState
import com.aamoza.vpn.data.EngineMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AamozaApp(
    onConnectRequest: () -> Unit,
    onDisconnectRequest: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(EngineMode.WARP) }
    var connectionState by remember { mutableStateOf(ConnectionState.DISCONNECTED) }
    var showAbout by remember { mutableStateOf(false) }
    var token by remember { mutableStateOf("") }
    var workerUrl by remember { mutableStateOf("") }

    val statusColor by animateColorAsState(
        targetValue = when (connectionState) {
            ConnectionState.CONNECTED -> Color(0xFF00C853)
            ConnectionState.CONNECTING -> Color(0xFFFFAB00)
            ConnectionState.DISCONNECTED -> Color(0xFF9E9E9E)
        },
        label = "status"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "aamoza vpn",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = { showAbout = true }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Mode selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ModeChip(
                    title = "WARP",
                    selected = selectedMode == EngineMode.WARP,
                    onClick = { selectedMode = EngineMode.WARP },
                    modifier = Modifier.weight(1f)
                )
                ModeChip(
                    title = "Worker",
                    selected = selectedMode == EngineMode.WORKER,
                    onClick = { selectedMode = EngineMode.WORKER },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Status circle
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                statusColor.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                FilledIconButton(
                    onClick = {
                        when (connectionState) {
                            ConnectionState.DISCONNECTED -> {
                                connectionState = ConnectionState.CONNECTING
                                onConnectRequest()
                                // Simulate connection for UI demo
                                // Real core will update state via binder/callback
                            }
                            ConnectionState.CONNECTED, ConnectionState.CONNECTING -> {
                                connectionState = ConnectionState.DISCONNECTED
                                onDisconnectRequest()
                            }
                        }
                    },
                    modifier = Modifier.size(120.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = statusColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when (connectionState) {
                    ConnectionState.DISCONNECTED -> "قطع"
                    ConnectionState.CONNECTING -> "در حال اتصال…"
                    ConnectionState.CONNECTED -> "متصل"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )

            Text(
                text = if (selectedMode == EngineMode.WARP) "Cloudflare WARP" else "Worker Path",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Config fields
            if (selectedMode == EngineMode.WARP) {
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text("توکن Cloudflare / WARP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                OutlinedTextField(
                    value = workerUrl,
                    onValueChange = { workerUrl = it },
                    label = { Text("آدرس Worker یا دامنه") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "ساخته شده توسط t.me/aamoza",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            title = { Text("درباره aamoza vpn") },
            text = {
                Text(
                    "نسخه ۱.۰.۰\n\n" +
                    "Dual-engine client\n" +
                    "WARP (MASQUE / WireGuard) + Worker\n\n" +
                    "ساخته شده توسط t.me/aamoza"
                )
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("باشه")
                }
            }
        )
    }
}

@Composable
private fun ModeChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "chip"
    )
    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "chipContent"
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bg
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = contentColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
