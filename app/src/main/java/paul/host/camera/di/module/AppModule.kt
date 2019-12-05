package paul.host.camera.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import paul.host.camera.App
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.db.dao.ProjectDao
import paul.host.camera.data.repository.ImageRepository
import paul.host.camera.data.repository.ProjectsRepository
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun provideApplication(): Application = app

    @Singleton
    @Provides
    fun provideProjectsRepository(projectDao: ProjectDao, imageRepository: ImageRepository) =
        ProjectsRepository(projectDao, imageRepository)

    @Singleton
    @Provides
    fun provideImageRepository(imageDao: ImageDao) = ImageRepository(imageDao)

}