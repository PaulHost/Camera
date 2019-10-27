package paul.host.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.service.LongExposureService
import paul.host.camera.util.ServiceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        ServiceManager.bind(applicationContext, LongExposureService::class.java)
    }
}
