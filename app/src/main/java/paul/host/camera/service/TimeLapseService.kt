package paul.host.camera.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import paul.host.camera.ui.ShotActivity


abstract class TimeLapseService(name: String) : IntentService(name) {
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var startTime = System.currentTimeMillis()

    var period: Long = 20000
        set(minutes) {
            minutes * (60000)
            field = minutes
        }

    init {
        Log.d(this::class.java.simpleName, "MY_LOG: init")
        handler.postDelayed(takePicture(), period)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(this::class.java.simpleName, "MY_LOG: onHandleIntent")
        period = intent?.getLongExtra(EXTRA_PERIOD, period) ?: period
        handler.postDelayed(takePicture(), period)
    }

    open fun takeShotIntent() = ShotActivity.getIntent(applicationContext)

    private fun takePicture(): Runnable = Runnable {
        if (System.currentTimeMillis() < endTime()) {
            handler.removeCallbacks(takePicture())
            Log.d(this::class.java.simpleName, "MY_LOG: opening ShotActivity")
            startActivity(takeShotIntent())
            handler.postDelayed(takePicture(), period)
        } else {
            ServiceManager.unbind(this::class.java)
        }
    }

    private fun endTime() = startTime + (period * 5)

    companion object {
        const val EXTRA_PERIOD = "EXTRA_PERIOD"
    }
}