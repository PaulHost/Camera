package paul.host.camera.ui.project

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import paul.host.camera.App
import paul.host.camera.common.util.rx.fromIoToMainThread
import paul.host.camera.common.util.secToMillis
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.data.repository.ProjectsRepository
import paul.host.camera.service.VideoService
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
    }.fromIoToMainThread()
        .doOnNext { projectModel = it }

    fun save(name: String, interval: Int, count: Int): Completable {
        isEdit = false
        projectModel = ProjectModel(
            id = projectId ?: UUID.randomUUID().toString(),
            name = name,
            interval = interval.secToMillis(),
            count = count
        )
        return repository.saveProject(projectModel!!).fromIoToMainThread()
    }

    fun deleteProject(): Completable = if (projectModel != null) {
        repository.deleteProject(projectModel!!)
    } else {
        Completable.complete()
    }.fromIoToMainThread()

}
