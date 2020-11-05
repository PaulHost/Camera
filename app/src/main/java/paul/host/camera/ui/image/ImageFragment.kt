package paul.host.camera.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import paul.host.camera.R
import paul.host.camera.ui.base.BaseFragment

class ImageFragment : BaseFragment() {

    companion object {
        const val ARG_IMAGE_ID = "ARG_IMAGE_ID"
    }

    private val viewModel by viewModel<ImageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
