package paul.host.camera

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import paul.host.camera.common.Constants
import paul.host.camera.service.LongExposureService
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.service.VideoService
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

        val intent = VideoService.getIntent(
            context = applicationContext,
            fps = 5,
            iName = "${Constants.FOLDERS.mediaDirFile(applicationContext)}/$${Constants.NAMES.TIME_LAPSE}",
            vName = "${Constants.FOLDERS.externalStorageDirFile()}/${Constants.NAMES.TIME_LAPSE}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(intent)
        } else {
            applicationContext.startService(intent)
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
