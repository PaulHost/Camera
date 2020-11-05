package paul.host.camera.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import paul.host.camera.R
import paul.host.camera.ui.image.ImageFragment
import paul.host.camera.ui.navigation.MainNavigationListener
import paul.host.camera.ui.project.ProjectFragment


class MainNavigationActivity : CameraActivity(), MainNavigationListener {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun goToProjectFromProjectsList(projectId: String?, isEdit: Boolean) =
        navController.navigate(
            R.id.action_projectsListFragment_to_projectFragment,
            Bundle().apply {
                putString(ProjectFragment.ARG_PROJECT_ID, projectId)
                putBoolean(ProjectFragment.ARG_IS_EDIT, isEdit)
            }
        )

    override fun goToImageFromCamera(imageId: String) = navController.navigate(
        R.id.action_cameraFragment_to_imageFragment,
        Bundle().apply {
            putString(ImageFragment.ARG_IMAGE_ID, imageId)
        }
    )

    override fun goToImageFromProject(imageId: String) = navController.navigate(
        R.id.action_projectFragment_to_imageFragment,
        Bundle().apply {
            putString(ImageFragment.ARG_IMAGE_ID, imageId)
        }
    )

}
