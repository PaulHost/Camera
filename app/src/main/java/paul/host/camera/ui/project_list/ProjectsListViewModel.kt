package paul.host.camera.ui.project_list

import androidx.lifecycle.ViewModel
import paul.host.camera.App
import paul.host.camera.common.util.rx.fromIoToMainThread
import paul.host.camera.data.repository.ProjectsRepository
import javax.inject.Inject

class ProjectsListViewModel : ViewModel() {
    @Inject
    lateinit var repository: ProjectsRepository

    init {
        App.component.inject(this)
    }

    fun getProjects() = repository.getProjects().fromIoToMainThread()
}
