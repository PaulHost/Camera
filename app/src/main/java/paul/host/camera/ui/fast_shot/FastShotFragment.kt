package paul.host.camera.ui.fast_shot

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import paul.host.camera.common.Constants
import paul.host.camera.ui.camera.CameraFragment
import timber.log.Timber
import java.io.File

class FastShotFragment : CameraFragment(), Runnable {

    private lateinit var viewModel: FastShotViewModel
    private var exposureSeconds: Long? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FastShotViewModel::class.java)
        val intent = activity?.intent
        viewModel.pictureName = intent?.getStringExtra(ARG_PICTURE_NAME)
        viewModel.projectId = intent?.getStringExtra(ARG_PROJECT_ID)
        exposureSeconds = intent?.getLongExtra(ARG_EXPOSURE_SEC, 1000L)
    }

    override fun onStart() {
        super.onStart()
        focusView.postDelayed(this, PHOTO_DELAY)
    }

    override fun run() {
        Timber.d("MY_LOG: run taking picture")
        focusView.removeCallbacks(this)
        if (fotoapparat.isAvailable(activeCamera.lensPosition)) {
            takePicture(viewModel.pictureName)
        } else {
            focusView.postDelayed(this, PHOTO_DELAY)
        }
    }

    override fun onImageSaved(file: File) {
        super.onImageSaved(file)
        viewModel.saveImageInfo(file)
        focusView.post { navigationListener?.closeCurrentActivity() }
    }

    companion object {
        private const val PHOTO_DELAY = 1000L
        const val ARG_PICTURE_NAME = "ARG_PICTURE_NAME"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
        const val ARG_EXPOSURE_SEC = "ARG_EXPOSURE_SEC"

        fun getIntent(
            projectId: String,
            pictureName: String,
            exposureSeconds: Long
        ) =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.DEEP_LINK_URL.FAST_SHOT_FRAGMENT)
            ).apply {
                putExtra(ARG_PROJECT_ID, projectId)
                putExtra(ARG_PICTURE_NAME, pictureName)
                putExtra(ARG_EXPOSURE_SEC, exposureSeconds)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
            }

        fun start(context: Context, projectId: String, pictureName: String, exposureSeconds: Long) =
            context.startActivity(getIntent(projectId, pictureName, exposureSeconds))
    }
}
