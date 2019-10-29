package paul.host.camera.service

import android.R
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import paul.host.camera.common.Constants
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.ui.ShotActivity
import timber.log.Timber


abstract class TimeLapseService(private val name: String) : Service(), Runnable {
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var startTime = System.currentTimeMillis()
    private var count = 50
    private var endTime = 0L
    private var period: Long = 24000
        set(minutes) {
            minutes * (60000)
            field = minutes
        }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("MY_LOG: onBind")
        return null
    }

    override fun run() {
        Timber.d("MY_LOG: takePicture")
        if (System.currentTimeMillis() < endTime) {
            handler.removeCallbacks(this)
            Timber.d("MY_LOG: opening ShotActivity")
            startActivity(takeShotIntent())
            handler.postDelayed(this, period)
        } else {
            Timber.d("MY_LOG: stop")
            ServiceManager.stop(this::class.java)
        }
    }

    open fun onHandleIntent(intent: Intent?) {
        Timber.d("MY_LOG: onHandleIntent")

        period = intent?.getLongExtra(EXTRA_PERIOD, period) ?: period
        count = intent?.getIntExtra(EXTRA_COUNT, count) ?: count
        startTime = intent?.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
            ?: System.currentTimeMillis()
        endTime = intent?.getLongExtra(EXTRA_END_TIME, startTime + (period * count))
            ?: startTime + (period * count)

        if (startTime < System.currentTimeMillis()) {
            handler.post(this)
        } else {
            handler.postDelayed(this, startTime - System.currentTimeMillis())
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("MY_LOG: onStartCommand")

        when (intent?.action) {
            Constants.ACTION.START_FOREGROUND_ACTION -> {
                Timber.d("MY_LOG: Start action")
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

                onHandleIntent(intent)
            }
            Constants.ACTION.STOP_FOREGROUND_ACTION -> {
                Timber.d("MY_LOG: Stop action")
                stopForeground(true)
                ServiceManager.stop(this::class.java)
            }
        }

        return START_STICKY
    }

    open fun takeShotIntent() = ShotActivity.getIntent(applicationContext).apply {
        Timber.d("MY_LOG: takeShotIntent")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
    }

    companion object {
        const val EXTRA_PERIOD = "EXTRA_PERIOD"
        const val EXTRA_COUNT = "EXTRA_COUNT"
        const val EXTRA_START_TIME = "EXTRA_COUNT"
        const val EXTRA_END_TIME = "EXTRA_COUNT"
    }
}