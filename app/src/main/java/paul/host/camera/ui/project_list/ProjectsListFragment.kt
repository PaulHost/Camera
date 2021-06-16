package paul.host.camera.ui.project_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.data.model.Status
import paul.host.camera.databinding.ProjectsListBinding
import paul.host.camera.ui.adapter.ProjectsAdapter
import paul.host.camera.ui.navigation.NavigationFragment

@SuppressLint("CheckResult")
class ProjectsListFragment : NavigationFragment() {

    private val viewModel by viewModel<ProjectsListViewModel>()
    private val projectsVo = ProjectListViewObject()
    private lateinit var adapter: ProjectsAdapter
    private lateinit var binding: ProjectsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProjectsListBinding.inflate(inflater, container, false)
        binding.projectsVo = projectsVo
        binding.rvInProgress.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompleted.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEditable.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProjectsAdapter(navigationListener)
        binding.rvInProgress.adapter = adapter
        binding.rvCompleted.adapter = adapter
        binding.rvEditable.adapter = adapter
        binding.btnCreate.setOnClickListener {
            navigationListener?.goToProjectFromProjectsList(null, true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProjects().subscribe(::setList, ::onError)
    }

    private fun setList(list: List<ProjectModel>) {
        if (list.isNotEmpty()) {
            projectsVo.isInProgressVisible =
                list.find { it.status == Status.IN_PROGRESS }?.let { true } ?: false
            projectsVo.isCompletedVisible =
                list.find { it.status == Status.COMPLETED }?.let { true } ?: false
            projectsVo.isEditableVisible =
                list.find { it.status == Status.EDITABLE }?.let { true } ?: false
            adapter.setList(list)
        }
    }

}
