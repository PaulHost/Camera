package paul.host.camera.service

import android.R
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import paul.host.camera.common.Constants
import paul.host.camera.common.util.ServiceManager
import timber.log.Timber


class VideoService : Service() {
    private val name = this::class.java.simpleName
    private val notificationBuilder = NotificationCompat.Builder(this, name)
    private val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("MY_LOG: onBind")
        return null
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
                val notification = notificationBuilder
                    .setContentTitle("Making Video")
                    .setTicker(name)
                    .setContentText(name)
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setChannelId(Constants.CHANNEL_ID)
                    .setLargeIcon(
                        Bitmap.createScaledBitmap(
                            icon, 128, 128, false
                        )
                    )
                    .setOngoing(true).build()

                TODO("Implementing of video creation")

                startForeground(Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE, notification)
            }
            Constants.ACTION.STOP_FOREGROUND_ACTION -> {
                Timber.d("MY_LOG: Stop action")
                stopForeground(true)
                ServiceManager.stop(this::class.java)
            }
        }

        return START_STICKY
    }

    fun onFinish() {
        Timber.d(Constants.SUCCESSFUL)
        notificationBuilder.setContentText(Constants.SUCCESSFUL)
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    fun onProgress(progress: Int) {
        notificationBuilder.setProgress(100, progress, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    fun onCancel() {
        Timber.d("onCancel")
        notificationBuilder.setContentText(Constants.CANCELED)
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    fun onError(message: String) {
        Timber.d("onError：$message")
        notificationBuilder.setContentText("Error: $message")
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    companion object {
        private const val EXTRA_IMAGE_NAME = "EXTRA_IMAGE_NAME"
        private const val EXTRA_VIDEO_NAME = "EXTRA_VIDEO_NAME"
        private const val EXTRA_FPS = "EXTRA_FPS"

        fun getIntent(context: Context) = Intent(context, VideoService::class.java)

        fun getIntent(
            context: Context,
            iName: String,
            fps: Int = 25,
            vName: String
        ) = getIntent(context).apply {
            putExtra(EXTRA_IMAGE_NAME, iName)
            putExtra(EXTRA_FPS, fps)
            putExtra(EXTRA_VIDEO_NAME, vName)
            action = Constants.ACTION.START_FOREGROUND_ACTION
        }

        fun startDefault(context: Context) {
            val intent = getIntent(
                context = context,
                fps = 25,
                iName = "${Constants.FOLDERS.mediaDirFile(context)}/$${Constants.NAMES.TIME_LAPSE}",
                vName = "${Constants.FOLDERS.externalStorageDirFile()}/${Constants.NAMES.TIME_LAPSE}"
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

}