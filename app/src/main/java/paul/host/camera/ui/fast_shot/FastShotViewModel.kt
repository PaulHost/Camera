package paul.host.camera.ui.fast_shot

import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import paul.host.camera.App
import paul.host.camera.data.model.ImageModel
import paul.host.camera.data.repository.ImageRepository
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

    fun saveImageInfo(file: File) = Flowable.just(
        ImageModel(
            projectId = projectId ?: throw NullPointerException("Project id is null"),
            name = pictureName ?: System.currentTimeMillis().toString(),
            path = file.absolutePath
        )
    ).flatMapCompletable(repository::saveImage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}
