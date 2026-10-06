package com.aamoza.vpn

import android.app.Application
import com.aamoza.vpn.core.notification.NotificationHelper
import com.aamoza.vpn.data.ConfigRepository
import com.aamoza.vpn.engine.NativeLibLoader

class AamozaApp : Application() {

    lateinit var configRepository: ConfigRepository
        private set

    override fun onCreate() {
        super.onCreate()
        configRepository = ConfigRepository(this)
        NativeLibLoader.load(this)
        NotificationHelper.ensureChannel(this)
    }
}
