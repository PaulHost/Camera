package paul.host.camera.ui.project_list

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import paul.host.camera.App
import paul.host.camera.data.repository.ProjectsRepository
import javax.inject.Inject

class ProjectsListViewModel : ViewModel() {
    @Inject
    lateinit var repository: ProjectsRepository

    init {
        App.component.inject(this)
    }

    fun getProjects() = repository.getProjects()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
