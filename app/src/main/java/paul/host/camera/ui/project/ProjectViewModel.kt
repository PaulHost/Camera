package paul.host.camera.ui.project

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import paul.host.camera.App
import paul.host.camera.common.util.secToMillis
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.data.repository.ProjectsRepository
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class ProjectViewModel : ViewModel() {
    @Inject
    lateinit var repository: ProjectsRepository
    var projectModel: ProjectModel? = null
    var isEdit = false
    var projectId: String? = null
        get() = projectModel?.id ?: field

    init {
        App.component.inject(this)
    }

    fun getProject() = if (projectId != null) {
        repository.getProject(projectId!!)
    } else {
        Flowable.empty()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext {
            projectModel = it
        }

    fun save(name: String, interval: Int, count: Int) {
        projectModel = ProjectModel(
            id = projectId ?: UUID.randomUUID().toString(),
            name = name,
            interval = interval.secToMillis(),
            count = count
        )
        repository.saveProject(projectModel!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Timber::e)
    }
}

