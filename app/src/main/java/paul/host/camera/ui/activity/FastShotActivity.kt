package paul.host.camera.ui.activity

import android.os.Bundle
import paul.host.camera.R
import paul.host.camera.ui.fast_shot.FastShotFragment


class FastShotActivity : CameraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, FastShotFragment())
            .commit()
    }

}
