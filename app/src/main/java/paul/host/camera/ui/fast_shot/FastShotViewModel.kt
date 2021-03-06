package paul.host.camera.ui.fast_shot

import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import paul.host.camera.App
import paul.host.camera.common.util.rx.fromIoToMainThread
import paul.host.camera.data.model.ImageModel
import paul.host.camera.data.repository.ImageRepository
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class FastShotViewModel : ViewModel() {
    @Inject
    lateinit var repository: ImageRepository

    var pictureName: String? = null
    var projectId: String? = null

    init {
        App.component.inject(this)
    }

    fun saveImageInfo(file: File, function: () -> Unit = {}) = Flowable.just(
        ImageModel(
            projectId = projectId ?: throw NullPointerException("Project id is null"),
            name = pictureName ?: System.currentTimeMillis().toString(),
            path = file.absolutePath
        )
    ).flatMapCompletable(repository::saveImage).fromIoToMainThread()
        .subscribe({ function() }, Timber::e)

}
