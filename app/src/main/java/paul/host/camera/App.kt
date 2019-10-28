package paul.host.camera

import android.app.Application
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
    }
}