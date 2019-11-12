@file:Suppress("DEPRECATION", "WakelockTimeout")

package paul.host.camera.common.util

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager


object WakeUpManager {
    fun wakeUp(context: Context) {
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!powerManager.isInteractive) {
            powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        or PowerManager.FULL_WAKE_LOCK
                        or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                this::class.java.simpleName
            ).acquire()
            keyguardManager.newKeyguardLock(this::class.java.simpleName)
                .disableKeyguard()
        }
    }
}