package paul.host.camera.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import paul.host.camera.common.Constants
import paul.host.camera.data.db.DataBase
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.db.dao.ProjectDao
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDataBase(app: Application): DataBase =
        Room.databaseBuilder(app, DataBase::class.java, Constants.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideProjectDao(db: DataBase): ProjectDao = db.projectDao()

    @Singleton
    @Provides
    fun provideImageDao(db: DataBase): ImageDao = db.imageDao()

}
