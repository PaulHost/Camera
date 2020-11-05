package paul.host.camera.ui.fast_shot

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import paul.host.camera.ui.activity.FastShotActivity
import paul.host.camera.ui.camera.CameraFragment
import timber.log.Timber
import java.io.File

@SuppressLint("CheckResult")
class FastShotFragment : CameraFragment(), Runnable {

    private val viewModel by viewModel<FastShotViewModel>()
    private var exposureSeconds: Long? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = activity?.intent
        viewModel.pictureName = intent?.getStringExtra(ARG_PICTURE_NAME)
        viewModel.projectId = intent?.getStringExtra(ARG_PROJECT_ID)
        exposureSeconds = intent?.getLongExtra(ARG_EXPOSURE, 1000L)
    }

    override fun onStart() {
        super.onStart()
        focusView.postDelayed(this, PHOTO_DELAY)
    }

    override fun run() {
        Timber.d("MY_LOG: run taking picture")
        focusView.removeCallbacks(this)
        if (fotoapparat.isAvailable()) takePicture(viewModel.pictureName)
        else focusView.postDelayed(this, PHOTO_DELAY)
    }

    override fun onImageSaved(file: File) {
        super.onImageSaved(file)
        viewModel.saveImageInfo(file) { activity?.finish() }
    }

    companion object {
        private const val PHOTO_DELAY = 1000L
        const val ARG_PICTURE_NAME = "ARG_PICTURE_NAME"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
        const val ARG_EXPOSURE = "ARG_EXPOSURE_SEC"

        private fun getIntent(
            context: Context,
            projectId: String,
            pictureName: String,
            exposureSeconds: Long
        ) = Intent(context, FastShotActivity::class.java).apply {
            putExtra(ARG_PROJECT_ID, projectId)
            putExtra(ARG_PICTURE_NAME, pictureName)
            putExtra(ARG_EXPOSURE, exposureSeconds)
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_NO_ANIMATION
                        or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            )
        }

        fun start(context: Context, projectId: String, pictureName: String, exposureSeconds: Long) =
            context.startActivity(
                getIntent(
                    context,
                    projectId,
                    pictureName,
                    exposureSeconds
                )
            )
    }
}
