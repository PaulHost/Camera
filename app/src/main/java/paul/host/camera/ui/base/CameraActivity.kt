package paul.host.camera.ui.base

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.R
import paul.host.camera.common.util.WakeUpManager
import paul.host.camera.ui.ShotFragment

class CameraActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shot_activity)
        if (savedInstanceState == null) showShotFragment()
    }

    private fun showShotFragment() = supportFragmentManager.beginTransaction()
        .replace(
            R.id.shot_activity_content,
            ShotFragment.getInstance(intent?.getStringExtra(EXTRA_PICTURE_NAME))
        ).commit()

    companion object {
        const val EXTRA_LONG_EXPOSURE_SHOT = "EXTRA_LONG_EXPOSURE_SHOT"
        const val EXTRA_PICTURE_NAME = "EXTRA_PICTURE_NAME"

        fun getIntent(context: Context, name: String? = null) = Intent(
            context,
            CameraActivity::class.java
        ).apply {
            putExtra(EXTRA_PICTURE_NAME, name)
        }
    }

}
