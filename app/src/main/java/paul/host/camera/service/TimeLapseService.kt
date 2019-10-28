package paul.host.camera.service

import android.R
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import paul.host.camera.Constants
import paul.host.camera.ui.ShotActivity
import paul.host.camera.util.ServiceManager


abstract class TimeLapseService(private val name: String) : Service() {
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var startTime = System.currentTimeMillis()
    private var count = 5
    private var endTime = 0L
    private var period: Long = 24000
        set(minutes) {
            minutes * (60000)
            field = minutes
        }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(name, "MY_LOG: onBind")
        onHandleIntent(intent)
        return null
    }

    open fun onHandleIntent(intent: Intent?) {
        Log.d(name, "MY_LOG: onHandleIntent")

        period = intent?.getLongExtra(EXTRA_PERIOD, period) ?: period
        count = intent?.getIntExtra(EXTRA_COUNT, count) ?: count
        startTime = intent?.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
            ?: System.currentTimeMillis()
        endTime = intent?.getLongExtra(EXTRA_END_TIME, startTime + (period * count))
            ?: startTime + (period * count)

        handler.post(takePicture())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(name, "MY_LOG: onStartCommand")

        onHandleIntent(intent)

        when (intent?.action) {
            Constants.ACTION.START_FOREGROUND_ACTION -> {
                Log.d(name, "MY_LOG: Start action")
                val icon: Bitmap = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_menu_camera
                )
                val notification = NotificationCompat.Builder(this, name)
                    .setContentTitle(name)
                    .setTicker("Camera Sticker")
                    .setContentText(name)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setLargeIcon(
                        Bitmap.createScaledBitmap(
                            icon, 128, 128, false
                        )
                    )
                    .setOngoing(true).build()

                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification)
            }
            Constants.ACTION.STOP_FOREGROUND_ACTION -> {
                Log.d(name, "MY_LOG: Stop action")
                stopForeground(true)
                ServiceManager.stop(this::class.java)
            }
        }

        return START_STICKY
    }

    open fun takeShotIntent() = ShotActivity.getIntent(applicationContext).apply {
        Log.d(name, "MY_LOG: takeShotIntent")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
    }

    private fun takePicture(): Runnable = Runnable {
        Log.d(name, "MY_LOG: takePicture")
        if (System.currentTimeMillis() < endTime) {
            handler.removeCallbacks(takePicture())
            Log.d(name, "MY_LOG: opening ShotActivity")
            startActivity(takeShotIntent())
            handler.postDelayed(takePicture(), period)
        } else {
            Log.d(name, "MY_LOG: stop")
            ServiceManager.stop(this::class.java)
        }
    }

    companion object {
        const val EXTRA_PERIOD = "EXTRA_PERIOD"
        const val EXTRA_COUNT = "EXTRA_COUNT"
        const val EXTRA_START_TIME = "EXTRA_COUNT"
        const val EXTRA_END_TIME = "EXTRA_COUNT"
    }
}