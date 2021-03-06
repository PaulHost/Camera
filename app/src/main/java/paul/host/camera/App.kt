package paul.host.camera

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import paul.host.camera.common.Constants
import paul.host.camera.di.AppComponent
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var component: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        component = AppComponent.Initializer.init(app = this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        createNitrificationChannel()
    }

    private fun createNitrificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}