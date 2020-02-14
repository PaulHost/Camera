package paul.host.camera.common.camera

import android.content.Context
import io.fotoapparat.Fotoapparat
import io.fotoapparat.result.WhenDoneListener
import io.fotoapparat.view.CameraRenderer
import io.fotoapparat.view.FocusView
import timber.log.Timber
import java.io.File

class FotoapparatWrapper(
    private val context: Context
) {
    private lateinit var fotoapparat: Fotoapparat
    private var camera: Camera = Camera.Front

    fun initCamera(
        view: CameraRenderer,
        focusView: FocusView
    ): FocusView {
        fotoapparat = Fotoapparat(
            context = context,
            view = view,
            focusView = focusView,
            lensPosition = camera.lensPosition,
            cameraConfiguration = camera.configuration,
            cameraErrorCallback = { Timber.e(it, "MY_LOG: Camera error:") }
        )

        return focusView
    }

    fun takePicture(file: File, onImageSaved: (File) -> Unit) = fotoapparat.takePicture().apply {
        saveToFile(file).whenDone(object : WhenDoneListener<Unit> {
            override fun whenDone(it: Unit?) {
                onImageSaved(file)
            }
        })
    }

    fun changeCamera() {
        Timber.d("MY_LOG: changeCamera")
        camera = when (camera) {
            Camera.Front -> Camera.Back
            Camera.Back -> Camera.Front
        }
        switchTo(camera)
    }

    fun start() = fotoapparat.start()

    fun stop() = fotoapparat.stop()

    private fun switchTo(camera: Camera) {
        this.camera = camera
        fotoapparat.switchTo(
            lensPosition = camera.lensPosition,
            cameraConfiguration = camera.configuration
        )
    }

}
