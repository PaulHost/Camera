package paul.host.camera.ui.project

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import paul.host.camera.App
import paul.host.camera.data.model.TimeLapseProjectModel
import paul.host.camera.data.repository.ProjectsRepository
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class ProjectViewModel : ViewModel() {
    @Inject
    lateinit var repository: ProjectsRepository
    private var projectModel: TimeLapseProjectModel? = null
    var isEdit = false
    var projectId: String? = null

    init {
        App.component.inject(this)
    }

    fun getProject() = if (projectId != null) {
        repository.getProject(projectId!!)
    } else {
        Flowable.empty()
    }
        .map { it as TimeLapseProjectModel }
        .doOnNext {
            projectModel = it
        }

    fun save(name: String, interval: Int, count: Int) {
        projectModel = TimeLapseProjectModel(
            id = projectId ?: UUID.randomUUID().toString(),
            name = name,
            interval = interval * 1000L,
            count = count
        )
        repository.saveProject(projectModel!!).subscribe({}, Timber::e)
    }
}

