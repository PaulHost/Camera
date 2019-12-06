package paul.host.camera.ui.project

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.project_fragment.*
import kotlinx.android.synthetic.main.project_fragment.view.*
import paul.host.camera.R
import paul.host.camera.common.util.millisToSeconds
import paul.host.camera.common.util.recreate
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.service.TimeLapseService
import paul.host.camera.ui.adapter.ImagesAdapter
import paul.host.camera.ui.navigation.NavigationFragment
import timber.log.Timber


@SuppressLint("CheckResult")
class ProjectFragment : NavigationFragment() {

    companion object {
        const val ARG_IS_EDIT = "ARG_IS_EDIT"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
    }

    private lateinit var viewModel: ProjectViewModel
    private lateinit var adapter: ImagesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.project_fragment, container, false).apply {
        setHasOptionsMenu(true)
        adapter = ImagesAdapter(navigationListener)
        images_list.layoutManager = LinearLayoutManager(requireContext())
        images_list.adapter = adapter
        viewModel.isEdit = arguments?.getBoolean(ARG_IS_EDIT) ?: false
        viewModel.projectId = arguments?.getString(ARG_PROJECT_ID)
        btn_start.setOnClickListener { start() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProject().subscribe(::setProject, ::onError)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.isEdit) {
            menu.add(
                R.id.product_menu_group,
                R.id.product_menu_save,
                Menu.FLAG_ALWAYS_PERFORM_CLOSE,
                R.string.save
            )
        } else {
            menu.add(
                R.id.product_menu_group,
                R.id.product_menu_edit,
                Menu.FLAG_ALWAYS_PERFORM_CLOSE,
                R.string.edit
            )
        }
        menu.add(
            R.id.product_menu_group,
            R.id.product_menu_delete_project,
            Menu.FLAG_ALWAYS_PERFORM_CLOSE,
            R.string.delete_project
        )
        menu.add(
            R.id.product_menu_group, R.id.product_menu_build_video,
            Menu.FLAG_ALWAYS_PERFORM_CLOSE, R.string.build_video
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.product_menu_save -> save()
            R.id.product_menu_edit -> viewModel.isEdit = true
            R.id.product_menu_delete_project -> deleteProject { this.recreate() }
            R.id.product_menu_build_video -> save()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setProject(project: ProjectModel) {
        val interval = project.interval.millisToSeconds().toString()
        val count = project.count.toString()
        et_name.setText(project.name)
        et_interval.setText(interval)
        et_count.setText(count)
        adapter.setList(project.images)
    }

    private fun start() = save { TimeLapseService.start(requireContext(), viewModel.projectId) }

    private fun save(function: () -> Unit = {}) = viewModel.save(
        et_name.text.toString(),
        et_interval.text.toString().toInt(),
        et_count.text.toString().toInt()
    ).subscribe({ function() }, Timber::e)


    private fun deleteProject(function: () -> Unit = {}) =
        viewModel.deleteProject().subscribe({ function() }, Timber::e)

}
