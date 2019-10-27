package paul.host.camera.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import paul.host.camera.ui.ShotActivity


abstract class TimeLapseService(name: String) : IntentService(name) {
    private var handler: Handler = Handler(Looper.getMainLooper())

    var period: Long = 1000
        set(minutes) {
            minutes * (60000)
            field = minutes
        }

    init {
        Log.d(this::class.java.simpleName, "init")
        handler.postDelayed(takePicture(), period)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(this::class.java.simpleName, "onHandleIntent")
        period = intent?.getLongExtra(EXTRA_PERIOD, period) ?: period
        handler.postDelayed(takePicture(), period)
    }

    open fun takeShotIntent() = ShotActivity.getIntent(applicationContext)

    private fun takePicture(): Runnable = Runnable {
        handler.removeCallbacks(takePicture())
        Log.d(this::class.java.simpleName, "opening ShotActivity")
        startActivity(takeShotIntent())
        handler.postDelayed(takePicture(), period)
    }

    companion object {
        const val EXTRA_PERIOD = "EXTRA_PERIOD"
    }
}