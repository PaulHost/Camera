package paul.host.camera.ui.project_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.projects_list_fragment.view.*
import paul.host.camera.R
import paul.host.camera.ui.adapter.ProjectsAdapter
import paul.host.camera.ui.navigation.NavigationFragment

@SuppressLint("CheckResult")
class ProjectsListFragment : NavigationFragment() {

    private val viewModel by viewModel<ProjectsListViewModel>()
    private lateinit var adapter: ProjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.projects_list_fragment, container, false).apply {
        adapter = ProjectsAdapter(navigationListener)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        fab_create.setOnClickListener {
            navigationListener?.goToProjectFromProjectsList(null, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProjects().subscribe(adapter::setList, ::onError)
    }

}
