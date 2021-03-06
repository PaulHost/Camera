package paul.host.camera.ui.navigation

interface MainNavigationListener {
    fun goToProjectFromProjectsList(projectId: String?, isEdit: Boolean)
    fun goToImageFromCamera(imageId: String)
    fun goToImageFromProject(imageId: String)
}