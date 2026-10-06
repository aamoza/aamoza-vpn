package com.aamoza.vpn.engine

import android.content.Context
import java.io.File

/**
 * لودر امن کتابخانه native. اگر libaamoza.so در jniLibs نباشد،
 * برنامه کرش نمی‌کند و موتورها با پیام واضح خطا می‌دهند.
 */
object NativeLibLoader {

    @Volatile
    var loaded: Boolean = false
        private set

    fun load(context: Context): Boolean {
        if (loaded) return true
        val libFile = File(context.applicationInfo.nativeLibraryDir, "libaamoza.so")
        if (!libFile.exists()) return false
        return try {
            System.loadLibrary("aamoza")
            loaded = true
            true
        } catch (t: Throwable) {
            false
        }
    }
}
