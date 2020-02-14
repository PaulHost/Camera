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
import io.fotoapparat.view.FocusView
import kotlinx.android.synthetic.main.camera_fragment.view.*
import paul.host.camera.R
import paul.host.camera.common.Constants
import paul.host.camera.common.camera.FotoapparatWrapper
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
    Manifest.permission.DISABLE_KEYGUARD,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

open class CameraFragment : NavigationFragment() {
    internal lateinit var focusView: FocusView
    internal lateinit var fotoapparat: FotoapparatWrapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.camera_fragment, container, false).apply {
        Timber.d("MY_LOG: onCreateView")
        fotoapparat = FotoapparatWrapper(requireContext())
        focusView = focus_view

        fotoapparat.initCamera(
            view = camera_view,
            focusView = focusView
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

    private fun takePicture(file: File) = fotoapparat.takePicture(file, ::onImageSaved)

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
