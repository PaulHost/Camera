package paul.host.camera.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import paul.host.camera.common.Constants
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.common.util.toImageName
import paul.host.camera.ui.fast_shot.FastShotFragment
import timber.log.Timber


open class TimeLapseService : Service(), Runnable {
    private var iterator = 0
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var startTime = System.currentTimeMillis()
    private var count = 50
    private var endTime = 0L
    private var interval: Long = 24000
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
            Timber.d("MY_LOG: opening FastShotFragment")
            takeShot()
            iterator++
            handler.postDelayed(this, interval)
        } else {
            Timber.d("MY_LOG: stop")
            application.stopService(getIntent(applicationContext))
        }
    }

    open fun onHandleIntent(intent: Intent?) {
        Timber.d("MY_LOG: onHandleIntent")

        interval = intent?.getLongExtra(EXTRA_INTERVAL, interval) ?: interval
        count = intent?.getIntExtra(EXTRA_COUNT, count) ?: count
        startTime = intent?.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
            ?: System.currentTimeMillis()
        endTime = intent?.getLongExtra(EXTRA_END_TIME, startTime + (interval * count))
            ?: startTime + (interval * count)

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
                    android.R.drawable.ic_menu_camera
                )
                val notification = NotificationCompat.Builder(this, "test")
                    .setContentTitle("test")
                    .setTicker("Camera Sticker")
                    .setContentText("test")
                    .setSmallIcon(android.R.drawable.ic_menu_camera)
                    .setLargeIcon(
                        Bitmap.createScaledBitmap(
                            icon, 128, 128, false
                        )
                    )
                    .setChannelId(Constants.CHANNEL_ID)
                    .setOngoing(true).build()

                startForeground(Constants.NOTIFICATION_ID.TIMELAPSE_SERVICE, notification)

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

    open fun takeShot() =
        FastShotFragment.start(
            applicationContext,
            "test",
            "test_${iterator.toImageName(count)}",
            1
        )

    companion object {
        const val EXTRA_INTERVAL = "EXTRA_INTERVAL"
        const val EXTRA_COUNT = "EXTRA_COUNT"
        const val EXTRA_START_TIME = "EXTRA_START_TIME"
        const val EXTRA_END_TIME = "EXTRA_END_TIME"

        fun getIntent(
            context: Context,
            action: String = Constants.ACTION.STOP_FOREGROUND_ACTION
        ) = Intent(context, TimeLapseService::class.java).apply {
            this.action = action
        }

        fun getIntent(
            context: Context,
            interval: Int = 24,
            count: Int? = null,
            startTime: Long = System.currentTimeMillis(),
            endTime: Long? = null
        ) = getIntent(
            context,
            Constants.ACTION.START_FOREGROUND_ACTION
        ).apply {
            putExtra(EXTRA_INTERVAL, interval)
            putExtra(EXTRA_COUNT, count)
            putExtra(EXTRA_START_TIME, startTime)
            putExtra(EXTRA_END_TIME, endTime)
        }

        fun start(context: Context) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(
                    getIntent(
                        context,
                        Constants.ACTION.START_FOREGROUND_ACTION
                    )
                )
            } else {
                context.startService(getIntent(context, Constants.ACTION.START_FOREGROUND_ACTION))
            }
    }
}