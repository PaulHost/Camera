package paul.host.camera.ui.project_list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import paul.host.camera.R

class ProjectsListFragment : Fragment() {

    companion object {
        fun newInstance() = ProjectsListFragment()
    }

    private lateinit var viewModel: ProjectsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.projects_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProjectsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
