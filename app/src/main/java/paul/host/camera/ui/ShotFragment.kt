package paul.host.camera.ui

import android.os.Bundle
import paul.host.camera.ui.base.CameraFragment
import timber.log.Timber
import java.io.File

open class ShotFragment : CameraFragment(), Runnable {

    override fun run() {
        Timber.d("MY_LOG: run taking picture")
        focusView.removeCallbacks(this)
        if (fotoapparat.isAvailable(activeCamera.lensPosition)) {
            takePicture()
        } else {
            focusView.postDelayed(this, PHOTO_DELAY)
        }
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MY_LOG: onStop")
        fotoapparat.stop()
    }

    override fun onImageSaved(file: File) {
        super.onImageSaved(file)
        focusView.post {
            activity?.finish()
        }
    }

    companion object {
        private const val PHOTO_DELAY = 1000L
        private const val ARG_PICTURE_NAME = "ARG_PICTURE_NAME"
        fun getInstance(name: String? = null) = ShotFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PICTURE_NAME, name)
            }
        }
    }
}
