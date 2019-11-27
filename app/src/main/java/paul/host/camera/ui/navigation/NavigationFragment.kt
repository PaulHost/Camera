package paul.host.camera.ui.navigation

import android.content.Context
import androidx.fragment.app.Fragment

open class NavigationFragment : Fragment() {
    var navigationListener: MainNavigationListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainNavigationListener) navigationListener = context
    }
}