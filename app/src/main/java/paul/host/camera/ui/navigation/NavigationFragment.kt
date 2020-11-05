package paul.host.camera.ui.navigation

import android.content.Context
import paul.host.camera.ui.base.BaseFragment

open class NavigationFragment : BaseFragment() {
    var navigationListener: MainNavigationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainNavigationListener) navigationListener = context
    }
}