@file:Suppress("MemberVisibilityCanBePrivate")

package paul.host.camera.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.result.WhenDoneListener
import io.fotoapparat.selector.*
import io.fotoapparat.view.CameraRenderer
import io.fotoapparat.view.FocusView
import kotlinx.android.synthetic.main.camera_fragment.view.*
import paul.host.camera.R
import paul.host.camera.common.Constants
import paul.host.camera.ui.navigation.NavigationFragment
import timber.log.Timber
import java.io.File

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

open class CameraFragment : NavigationFragment() {
    internal lateinit var focusView: FocusView
    internal lateinit var fotoapparat: Fotoapparat
    internal var activeCamera: Camera = Camera.Back

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.camera_fragment, container, false).apply {
        Timber.d("MY_LOG: onCreateView")

        initFotoapparat(
            view = camera_view,
            focusView = focus_view
        )

        start_button.setOnClickListener {
            navigationListener?.goToProjectsListScreen()
        }

        // Request camera permissions
        if (allPermissionsGranted()) focusView.post {
            fotoapparat.start()
        } else this@CameraFragment.activity?.let {
            ActivityCompat.requestPermissions(it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    open fun initFotoapparat(view: CameraRenderer, focusView: FocusView) {
        fotoapparat = Fotoapparat(
            context = requireContext(),
            view = view,
            focusView = focusView,
            lensPosition = activeCamera.lensPosition,
            cameraConfiguration = activeCamera.configuration,
            cameraErrorCallback = { Timber.e(it, "MY_LOG: Camera error:") }
        )

        this.focusView = focusView
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MY_LOG: onStart")
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MY_LOG: onStop")
        fotoapparat.stop()
    }

    open fun onImageSaved(file: File) {
        Timber.d("MY_LOG: saved: ${file.absolutePath}")
    }

    fun takePicture(name: String? = null) = takePicture(
        File(
            Constants.FOLDERS.mediaDirFile(requireContext()),
            "${name ?: System.currentTimeMillis()}.jpg"
        )
    )

    fun changeCamera() {
        Timber.d("MY_LOG: changeCamera")
        activeCamera = when (activeCamera) {
            Camera.Front -> Camera.Back
            Camera.Back -> Camera.Front
        }

        fotoapparat.switchTo(
            lensPosition = activeCamera.lensPosition,
            cameraConfiguration = activeCamera.configuration
        )
    }

    private fun takePicture(file: File) = fotoapparat.takePicture().apply {
        saveToFile(file).whenDone(object : WhenDoneListener<Unit> {
            override fun whenDone(it: Unit?) {
                onImageSaved(file)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                focusView.post { fotoapparat.start() }
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
}

sealed class Camera(
    val lensPosition: LensPositionSelector,
    val configuration: CameraConfiguration
) {

    object Back : Camera(
        lensPosition = back(),
        configuration = CameraConfiguration(
            previewResolution = firstAvailable(
                wideRatio(highestResolution()),
                standardRatio(highestResolution())
            ),
            previewFpsRange = highestFps(),
            flashMode = off(),
            focusMode = firstAvailable(
                continuousFocusPicture(),
                autoFocus()
            ),
            frameProcessor = {
                // ToDo something with the preview frame
            }
        )
    )

    object Front : Camera(
        lensPosition = front(),
        configuration = CameraConfiguration(
            previewResolution = firstAvailable(
                wideRatio(highestResolution()),
                standardRatio(highestResolution())
            ),
            previewFpsRange = highestFps(),
            flashMode = off(),
            focusMode = firstAvailable(
                fixed(),
                autoFocus()
            )
        )
    )
}
