package paul.host.camera.ui.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.project_fragment.*
import kotlinx.android.synthetic.main.project_fragment.view.*
import paul.host.camera.R
import paul.host.camera.data.model.TimeLapseProjectModel
import paul.host.camera.service.TimeLapseService
import paul.host.camera.ui.adapter.ImagesAdapter
import paul.host.camera.ui.navigation.NavigationFragment


@SuppressLint("CheckResult")
class ProjectFragment : NavigationFragment() {

    companion object {
        const val ARG_IS_EDIT = "ARG_IS_EDIT"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
    }

    private lateinit var viewModel: ProjectViewModel
    private lateinit var adapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.project_fragment, container, false).apply {
        adapter = ImagesAdapter(navigationListener)
        images_list.adapter = adapter
        viewModel = ViewModelProviders.of(this@ProjectFragment).get(ProjectViewModel::class.java)
        viewModel.isEdit = arguments?.getBoolean(ARG_IS_EDIT) ?: false
        viewModel.projectId = arguments?.getString(ARG_PROJECT_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProject().subscribe(::setProject, ::onError)
    }

    private fun setProject(project: TimeLapseProjectModel) {
        val interval = (project.interval / 60000).toString()
        val count = project.count.toString()
        et_name.setText(project.name)
        et_interval.setText(interval)
        et_count.setText(count)
        adapter.setList(project.images)
    }

    private fun start() {
        save()
        TimeLapseService.start(requireContext(), viewModel.projectId)
    }

    private fun save() {
        viewModel.save(
            et_name.text.toString(),
            et_interval.text.toString().toInt(),
            et_count.text.toString().toInt()
        )
    }
}
