package paul.host.camera.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.R
import paul.host.camera.ui.longexposure.LongExposureFragment

class LongExposureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.long_exposure_activity)
        when (intent.extras?.get(TAKE_PICTURE)) {
            null -> if (savedInstanceState == null) showLongExposureScreen()
        }

    }

    fun showLongExposureScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LongExposureFragment.newInstance())
            .commitNow()
    }

    companion object {
        private const val TAKE_PICTURE = "TAKE_PICTURE"
        fun getIntent(context: Context) = Intent(context, LongExposureActivity::class.java)

        fun getTakePictureIntent(context: Context, pictureName: String) = getIntent(context).apply {
            putExtra(TAKE_PICTURE, pictureName)
        }
    }

}
