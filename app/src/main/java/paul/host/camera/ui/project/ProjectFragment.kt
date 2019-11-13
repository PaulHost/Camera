package paul.host.camera.ui.project

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import paul.host.camera.R

class ProjectFragment : Fragment() {

    companion object {
        const val ARG_IS_EDIT = "ARG_IS_EDIT"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
    }

    private lateinit var viewModel: ProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.project_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
