package paul.host.camera.ui.fast_shot

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import paul.host.camera.common.Constants
import paul.host.camera.ui.camera.CameraFragment
import timber.log.Timber
import java.io.File

class FastShotFragment : CameraFragment(), Runnable {

    private lateinit var viewModel: FastShotViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FastShotViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        focusView.postDelayed(this, PHOTO_DELAY)
    }

    override fun run() {
        Timber.d("MY_LOG: run taking picture")
        focusView.removeCallbacks(this)
        if (fotoapparat.isAvailable(activeCamera.lensPosition)) {
            takePicture(arguments?.getString(ARG_PICTURE_NAME))
        } else {
            focusView.postDelayed(this, PHOTO_DELAY)
        }
    }

    override fun onImageSaved(file: File) {
        super.onImageSaved(file)
        focusView.post {
            arguments?.apply {
                mainListener?.goToProjectFromFastShot(
                    getString(
                        ARG_PROJECT_ID,
                        Constants.EMPTY_STRING
                    )
                )
            }
        }
    }

    companion object {
        private const val PHOTO_DELAY = 1000L
        const val ARG_PICTURE_NAME = "ARG_PICTURE_NAME"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
        const val ARG_EXPOSURE_SEC = "ARG_EXPOSURE_SEC"
    }
}
