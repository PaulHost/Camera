package paul.host.camera.ui

interface MainListener {
    fun goToProjectsListScreen()
    fun goToProjectFromProjectsList(projectId: String, isEdit: Boolean)
    fun goToProjectFromFastShot(projectId: String)
    fun goToImageFromCamera(imageId: String)
    fun goToImageFromProject(imageId: String)
}