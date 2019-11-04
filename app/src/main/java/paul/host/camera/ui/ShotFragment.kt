@file:Suppress("MemberVisibilityCanBePrivate")

package paul.host.camera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.fotoapparat.Fotoapparat
import io.fotoapparat.result.WhenDoneListener
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import io.fotoapparat.view.FocusView
import paul.host.camera.R
import paul.host.camera.common.Constants
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

open class ShotFragment : Fragment(), Runnable {
    private lateinit var focusView: FocusView
    private lateinit var fotoapparat: Fotoapparat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.shot_fragment, container, false).apply {
        Timber.d("MY_LOG: onCreateView")

        fotoapparat = Fotoapparat(
            context = requireContext(),
            view = findViewById<CameraView>(R.id.camera_view)
        )

        focusView = findViewById(R.id.focus_view)

        // Request camera permissions
        if (allPermissionsGranted()) focusView.post {
            fotoapparat.start()
        } else this@ShotFragment.activity?.let {
            ActivityCompat.requestPermissions(it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MY_LOG: onStart")
        fotoapparat.start()
        focusView.postDelayed(this, PHOTO_DELAY)
    }

    override fun run() {
        Timber.d("MY_LOG: run")
        focusView.removeCallbacks(this)
        if (fotoapparat.isAvailable(back())) {
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

    fun onImageSaved(file: File) {
        Timber.d("MY_LOG: saved: ${file.absolutePath}")
        focusView.post {
            activity?.finish()
        }
    }

    fun takePicture() = takePicture(
        File(
            Constants.FOLDERS.mediaDirFile(requireContext()),
            "${arguments?.get(ARG_PICTURE_NAME) ?: System.currentTimeMillis()}.jpg"
        )
    )

    fun takePicture(file: File) = fotoapparat.takePicture().apply {
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


