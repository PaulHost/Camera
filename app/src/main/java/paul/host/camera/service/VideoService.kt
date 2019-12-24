package paul.host.camera.service

import android.R
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.reactivex.Completable
import paul.host.camera.App
import paul.host.camera.common.Constants
import paul.host.camera.common.gif.RxGif
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.common.util.rx.newThread
import paul.host.camera.data.model.ImageModel
import paul.host.camera.data.repository.ImageRepository
import timber.log.Timber
import javax.inject.Inject


class VideoService : Service() {
    @Inject
    lateinit var repository: ImageRepository
    private lateinit var imges: List<ImageModel>
    private val name = this::class.java.simpleName
    private val notificationBuilder = NotificationCompat.Builder(this, name)
    private lateinit var notificationManager: NotificationManager

    init {
        App.component.inject(this)
    }

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
                notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

                handleIntent(intent)?.newThread()?.doOnComplete {
                    stop(this)
                }?.subscribe({
                    startForeground(Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE, notification)
                }, ::onError)
            }
            Constants.ACTION.STOP_FOREGROUND_ACTION -> {
                Timber.d("MY_LOG: Stop action")
                stopForeground(true)
                ServiceManager.stop(this::class.java)
            }
        }

        return START_STICKY
    }

    private fun handleIntent(intent: Intent) = intent.getStringExtra(EXTRA_PROJECT_ID)
        ?.let { id ->
            repository.getImages(id).doFinally {
                onFinish()
            }.doOnNext {
                imges = it
            }.flatMapCompletable {
                Timber.d("MY_LOG: handleIntent: list_size=%s", it.size)
                if (intent.getBooleanExtra(EXTRA_IS_GIF, false)) {
                    createGif(imges.first().name)
                } else {
                    createVideo()
                }
            }
        }

    private fun createVideo(): Completable = Completable.complete()

    private fun createGif(name: String): RxGif {
        Timber.d("MY_LOG: createGif")
        var bitmap: Bitmap
        return RxGif(name).apply {
            imges.forEachIndexed { i, image ->
                val percent = (i + 1) * 100 / imges.size
                Timber.d("MY_LOG:  $percent${'%'} image=${image.path}")
                onProgress(percent)
                bitmap = BitmapFactory.decodeFile(image.path, BitmapFactory.Options().apply {
                    inSampleSize = 8
                })
                onNext(bitmap)
                bitmap.recycle()
            }
        }
    }

    private fun onFinish() {
        Timber.d("MY_LOG: onFinish")
        Timber.d(Constants.SUCCESSFUL)
        notificationBuilder.setContentText(Constants.SUCCESSFUL)
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    private fun onProgress(progress: Int) {
        Timber.d("MY_LOG: onProgress")
        notificationBuilder.setProgress(100, progress, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    private fun onCancel() {
        Timber.d("MY_LOG: onCancel")
        notificationBuilder.setContentText(Constants.CANCELED)
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
        stop(this)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        notificationBuilder.setContentText("Error: ${throwable.message}")
            .setProgress(0, 0, false)
        notificationManager.notify(
            Constants.NOTIFICATION_ID.VIDEO_MAKER_SERVICE,
            notificationBuilder.build()
        )
    }

    companion object {
        private const val EXTRA_IMAGE_NAME = "EXTRA_IMAGE_NAME"
        private const val EXTRA_VIDEO_NAME = "EXTRA_VIDEO_NAME"
        private const val EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID"
        private const val EXTRA_IS_GIF = "EXTRA_IS_GIF"
        private const val EXTRA_FPS = "EXTRA_FPS"

        fun getIntent(context: Context) = Intent(context, VideoService::class.java)

        fun getIntent(context: Context, projectId: String, isGif: Boolean) =
            getIntent(context).putExtra(EXTRA_IS_GIF, isGif)
                .putExtra(EXTRA_PROJECT_ID, projectId)
                .setAction(Constants.ACTION.START_FOREGROUND_ACTION)

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

        fun startGif(context: Context, projectId: String) =
            start(context, getIntent(context, projectId, true))

        fun start(context: Context, intent: Intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) = context.stopService(
            getIntent(context).setAction(Constants.ACTION.STOP_FOREGROUND_ACTION)
        )
    }

}