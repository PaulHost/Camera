package paul.host.camera.ui.fast_shot

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import paul.host.camera.App
import paul.host.camera.data.model.ImageModel
import paul.host.camera.data.repository.ImageRepository
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class FastShotViewModel : ViewModel() {
    @Inject
    lateinit var repository: ImageRepository
    lateinit var image: ImageModel

    var pictureName: String? = null
    var projectId: String? = null

    init {
        App.component.inject(this)
    }

    @SuppressLint("CheckResult")
    fun saveImageInfo(file: File) {
        image = ImageModel(
            projectId = projectId ?: throw NullPointerException("Project id is null"),
            name = pictureName ?: System.currentTimeMillis().toString(),
            path = file.absolutePath
        )
        repository.saveImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, Timber::e)
    }

}
