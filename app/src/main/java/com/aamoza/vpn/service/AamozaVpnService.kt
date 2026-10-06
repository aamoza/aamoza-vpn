package com.aamoza.vpn.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.aamoza.vpn.MainActivity
import com.aamoza.vpn.R

/**
 * Foundation VpnService for aamoza vpn.
 *
 * Real tunnel logic (WARP/MASQUE or Worker/Xray) should be plugged in here.
 * Recommended cores:
 *  - Aether / Oblivion-style for WARP path
 *  - Xray-core or sing-box for Worker path
 */
class AamozaVpnService : VpnService() {

    companion object {
        const val ACTION_CONNECT = "com.aamoza.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.aamoza.vpn.DISCONNECT"
        private const val CHANNEL_ID = "aamoza_vpn_channel"
        private const val NOTIFICATION_ID = 1
    }

    private var tunInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> startTunnel()
            ACTION_DISCONNECT -> stopTunnel()
        }
        return START_STICKY
    }

    private fun startTunnel() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification("در حال اتصال…"))

        // --- Placeholder tunnel setup ---
        // In production: start selected engine (WARP or Worker),
        // configure Builder with routes/DNS, protect sockets, hand fd to core.
        try {
            val builder = Builder()
                .setSession("aamoza vpn")
                .addAddress("10.0.0.2", 32)
                .addDnsServer("1.1.1.1")
                .addDnsServer("1.0.0.1")
                .addRoute("0.0.0.0", 0)
                .setMtu(1280)
                .setBlocking(true)

            tunInterface = builder.establish()

            // TODO: hand tunInterface.fd to native core (Aether / Xray / sing-box)

            updateNotification("متصل — aamoza vpn")
        } catch (e: Exception) {
            e.printStackTrace()
            stopTunnel()
        }
    }

    private fun stopTunnel() {
        try {
            tunInterface?.close()
        } catch (_: Exception) {
        }
        tunInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "aamoza vpn",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "وضعیت اتصال aamoza vpn"
        }
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    private fun buildNotification(content: String): Notification {
        val openIntent = Intent(this, MainActivity::class.java)
        val pending = PendingIntent.getActivity(
            this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("aamoza vpn")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentIntent(pending)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(content: String) {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIFICATION_ID, buildNotification(content))
    }

    override fun onDestroy() {
        stopTunnel()
        super.onDestroy()
    }
}
