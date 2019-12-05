package paul.host.camera.service

import android.annotation.SuppressLint
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
import paul.host.camera.App
import paul.host.camera.common.Constants
import paul.host.camera.common.util.ServiceManager
import paul.host.camera.common.util.toImageName
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.data.repository.ProjectsRepository
import paul.host.camera.ui.fast_shot.FastShotFragment
import timber.log.Timber
import javax.inject.Inject


open class TimeLapseService : Service(), Runnable {
    @Inject
    lateinit var repository: ProjectsRepository
    private lateinit var project: ProjectModel
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var iterator = 0

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("MY_LOG: onBind")
        return null
    }

    init {
        Timber.d("init")
        App.component.inject(this)
    }

    override fun run() {
        Timber.d("MY_LOG: takePicture")
        if (System.currentTimeMillis() < project.endTime) {
            handler.removeCallbacks(this)
            Timber.d("MY_LOG: opening FastShotFragment")
            takeShot()
            iterator++
            handler.postDelayed(this, project.interval)
        } else {
            Timber.d("MY_LOG: stop")
            application.stopService(getIntent(applicationContext))
        }
    }

    @SuppressLint("CheckResult")
    open fun onHandleIntent(intent: Intent?) {
        Timber.d("MY_LOG: onHandleIntent")
        intent?.getStringExtra(EXTRA_PROJECT_ID)?.let(repository::getProject)?.subscribe({
            project = ProjectModel(it)
            if (project.startTime < System.currentTimeMillis()) {
                handler.post(this)
            } else {
                handler.postDelayed(this, project.startTime - System.currentTimeMillis())
            }
        }, Timber::e)
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
            project.id,
            "${project.name}_${iterator.toImageName(project.count)}",
            project.exposureTime
        )

    companion object {
        const val EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID"

        fun getIntent(
            context: Context,
            action: String = Constants.ACTION.STOP_FOREGROUND_ACTION,
            projectId: String? = null
        ) = Intent(context, TimeLapseService::class.java).apply {
            this.action = action
            putExtra(EXTRA_PROJECT_ID, projectId)
        }

        fun start(context: Context, projectId: String? = null) {
            val intent = getIntent(context, Constants.ACTION.START_FOREGROUND_ACTION, projectId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }
}