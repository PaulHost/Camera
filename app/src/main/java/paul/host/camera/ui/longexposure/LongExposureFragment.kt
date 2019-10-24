package paul.host.camera.ui.longexposure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import paul.host.camera.R

class LongExposureFragment : Fragment() {

    private lateinit var viewModel: LongExposureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.long_exposure_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LongExposureViewModel::class.java)
    }

    companion object {
        fun newInstance() = LongExposureFragment()
    }

}
