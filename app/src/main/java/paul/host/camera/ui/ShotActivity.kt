package paul.host.camera.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.R

class ShotActivity : AppCompatActivity() {

//    TODO: Camera implementation should be here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.long_exposure_activity)
    }

    companion object {
        const val EXTRA_LONG_EXPOSURE_SHOT = "EXTRA_LONG_EXPOSURE_SHOT"
        fun getIntent(context: Context) = Intent(context, ShotActivity::class.java)
    }

}
