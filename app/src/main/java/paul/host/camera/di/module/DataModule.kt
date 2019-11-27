package paul.host.camera.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import paul.host.camera.data.db.DataBase
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDataBase(app: Application) =
        Room.databaseBuilder(app, DataBase::class.java, "database").build()

    @Singleton
    @Provides
    fun provideProjectDao(db: DataBase) = db.projectDao()

}
