package paul.host.camera.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import timber.log.Timber

open class BaseFragment : Fragment() {

    fun onError(throwable: Throwable) {
        Timber.e(throwable)
    }

    protected inline fun <reified T : ViewModel> viewModel() = lazy {
        ViewModelProviders.of(this).get(T::class.java)
    }
}