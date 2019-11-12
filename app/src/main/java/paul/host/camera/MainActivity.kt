package paul.host.camera

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.service.LongExposureService
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var started = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.main_button)
        if (started) {
            button.text = "Stop"
        } else {
            button.text = "Start"
        }
    }

    fun startTimeLapseClick(view: View) {
        val v = view as Button
        Timber.d("MY_LOG: ${if (started) "stop" else "start"} button click")
        if (!started) {
            ServiceManager.start(applicationContext, LongExposureService::class.java)
            v.text = "Stop"
        } else {
            ServiceManager.stop(LongExposureService::class.java)
            v.text = "Start"
        }
        started = !started
    }
}
