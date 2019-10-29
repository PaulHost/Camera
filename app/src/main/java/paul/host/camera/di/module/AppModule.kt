package paul.host.camera.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import paul.host.camera.App
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun provideApplication(): Application = app
}