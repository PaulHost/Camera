@file:Suppress("MemberVisibilityCanBePrivate")

package paul.host.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.view.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import paul.host.camera.R
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors

// This is an arbitrary number we are using to keep track of the permission
// request. Where an app has multiple context for requesting permission,
// this can help differentiate the different contexts.
private const val REQUEST_CODE_PERMISSIONS = 10

// This is an array of all the permission specified in the manifest.
private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WAKE_LOCK,
    Manifest.permission.DISABLE_KEYGUARD
)

open class ShotFragment : Fragment(), LifecycleOwner, ImageCapture.OnImageSavedListener {

    private lateinit var viewFinder: TextureView
    private val _handler = Handler(Looper.getMainLooper())

    private val executor = Executors.newSingleThreadExecutor()
    private val imageCapture = ImageCapture(ImageCaptureConfig.Builder().apply {
        setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
    }.build())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.shot_fragment, container, false).apply {
        Timber.d("MY_LOG: onCreateView")

        viewFinder = findViewById(R.id.texture_view_finder)

        // Request camera permissions
        if (allPermissionsGranted()) viewFinder.post {
            startCamera()
        } else this@ShotFragment.activity?.let {
            ActivityCompat.requestPermissions(it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Every time the provided texture view changes, recompute layout
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("MY_LOG: onViewCreated")
        _handler.postDelayed(cameraReadyDaley(), 1000)
    }

    override fun onImageSaved(file: File) {
        Timber.d("MY_LOG: Photo capture succeeded: ${file.absolutePath}")
        viewFinder.post {
            activity?.finish()
        }
    }

    override fun onError(
        imageCaptureError: ImageCapture.ImageCaptureError,
        message: String,
        exc: Throwable?
    ) {
        Timber.e("MY_LOG: Photo capture failed: $message")
        viewFinder.post {
            activity?.finish()
        }
    }

    open fun cameraReadyDaley(): Runnable = Runnable {
        _handler.removeCallbacks(cameraReadyDaley())
        if (CameraX.isBound(imageCapture)) {
            takePicture()
        } else {
            _handler.postDelayed(cameraReadyDaley(), 1000)
        }
    }

    fun takePicture() = takePicture(
        File(
            activity?.externalMediaDirs?.first(),
            "${arguments?.get(ARG_PICTURE_NAME) ?: System.currentTimeMillis()}.jpg"
        )
    )

    fun takePicture(file: File) = imageCapture.takePicture(file, executor, this)

    private fun startCamera() {
        CameraX.bindToLifecycle(this, preview(), imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }

    open fun preview() = PreviewConfig.Builder().apply {
        setTargetResolution(previewTargetResolution())
    }.build().let { config ->
        val preview = Preview(config)
        preview.setOnPreviewOutputUpdateListener { output ->
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = output.surfaceTexture
            updateTransform()
        }
        preview
    }

    private fun previewTargetResolution(): Size {
        val h = 1280
        val w = 960
        return when (viewFinder.display.rotation) {
            Surface.ROTATION_0,
            Surface.ROTATION_270 -> Size(w, h)
            Surface.ROTATION_90,
            Surface.ROTATION_180 -> Size(h, w)
            else -> Size(viewFinder.width, viewFinder.height)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Timber.d("MY_LOG: onRequestPermissionsResult denied")
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val ARG_PICTURE_NAME = "ARG_PICTURE_NAME"
        fun getInstance(name: String? = null) = ShotFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PICTURE_NAME, name)
            }
        }
    }
}


