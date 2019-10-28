package paul.host.camera.di

import dagger.Component
import paul.host.camera.App
import paul.host.camera.di.module.AppModule
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {

    object Initializer {
        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }
    }
}