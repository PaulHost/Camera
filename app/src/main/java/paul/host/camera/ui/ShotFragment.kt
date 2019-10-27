package paul.host.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import paul.host.camera.R
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

open class ShotFragment : Fragment(), LifecycleOwner {

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
        Log.d(this::class.java.simpleName, "MY_LOG: onCreateView")

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
        Log.d(this::class.java.simpleName, "MY_LOG: onViewCreated")
        _handler.postDelayed(cameraReadyDalay(), 1000)
    }

    fun cameraReadyDalay(): Runnable = Runnable {
        _handler.removeCallbacks(cameraReadyDalay())
        if (CameraX.isBound(imageCapture)) {
            takePicture()
        } else {
            _handler.postDelayed(cameraReadyDalay(), 1000)
        }
    }

    private fun startCamera() {
        CameraX.bindToLifecycle(this, preview(), imageCapture)
    }

    fun takePicture(name: String = "${System.currentTimeMillis()}") = takePicture(
        File(
            activity?.externalMediaDirs?.first(),
            "${name}.jpg"
        )
    )

    fun takePicture(file: File) = imageCapture.takePicture(file, executor,
        object : ImageCapture.OnImageSavedListener {
            override fun onError(
                imageCaptureError: ImageCapture.ImageCaptureError,
                message: String,
                exc: Throwable?
            ) {
                Log.e(this::class.java.simpleName, "MY_LOG: Photo capture failed: $message")
                viewFinder.post {
                    activity?.finish()
                }
            }

            override fun onImageSaved(file: File) {
                val msg = "Photo capture succeeded: ${file.absolutePath}"
                Log.d(this::class.java.simpleName, "MY_LOG: $msg")
                viewFinder.post {
                    activity?.finish()
                }
            }
        }
    )

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

    private fun preview() = Preview(
        PreviewConfig.Builder().apply {
            setTargetResolution(previewTargetResolution())
        }.build()
    ).apply {
        setOnPreviewOutputUpdateListener {
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }
    }

    private fun previewTargetResolution() = Size(viewFinder.width / 2, viewFinder.height / 2)

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Log.d(this::class.java.simpleName, "MY_LOG: onRequestPermissionsResult denied")
                activity?.finish()
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun getInstance() = ShotFragment()
    }
}


