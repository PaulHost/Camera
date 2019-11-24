package paul.host.camera.ui.camera

import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.common.util.WakeUpManager

abstract class CameraActivity : AppCompatActivity() {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        WakeUpManager.wakeUp(applicationContext)
    }
}
