package paul.host.camera.ui.project_list

import paul.host.camera.ui.base.BaseViewObject

class ProjectListViewObject(
    var isInProgressVisible: Boolean = false,
    var isCompletedVisible: Boolean = false,
    var isEditableVisible: Boolean = false,
    var isInProgressOpen: Boolean = isInProgressVisible,
    var isCompletedOpen: Boolean = false,
    var isEditableOpen: Boolean = false
) : BaseViewObject() {
    fun onInProgressClick() = notifyChangeFunction {
        isInProgressOpen = !isInProgressOpen
    }

    fun onCompletedClick() = notifyChangeFunction {
        isCompletedOpen = !isCompletedOpen
    }

    fun onEditableOnClick() = notifyChangeFunction {
        isEditableOpen = !isEditableOpen
    }
}