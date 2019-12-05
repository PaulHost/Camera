package paul.host.camera.di

import dagger.Component
import paul.host.camera.App
import paul.host.camera.di.module.AppModule
import paul.host.camera.di.module.DataModule
import paul.host.camera.service.TimeLapseService
import paul.host.camera.ui.fast_shot.FastShotViewModel
import paul.host.camera.ui.project.ProjectViewModel
import paul.host.camera.ui.project_list.ProjectsListViewModel
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class
    ]
)
interface AppComponent {
    fun inject(viewModel: ProjectViewModel)
    fun inject(viewModel: ProjectsListViewModel)
    fun inject(service: TimeLapseService)
    fun inject(viewModel: FastShotViewModel)

    object Initializer {
        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .dataModule(DataModule())
                .build()
        }
    }
}