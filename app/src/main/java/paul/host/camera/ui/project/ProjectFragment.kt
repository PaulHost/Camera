package paul.host.camera.ui.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.project_fragment.*
import kotlinx.android.synthetic.main.project_fragment.view.*
import paul.host.camera.R
import paul.host.camera.common.util.millisToSeconds
import paul.host.camera.common.util.recreate
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.service.TimeLapseService
import paul.host.camera.service.VideoService
import paul.host.camera.ui.adapter.ImagesAdapter
import paul.host.camera.ui.navigation.NavigationFragment
import timber.log.Timber


@SuppressLint("CheckResult")
class ProjectFragment : NavigationFragment() {

    companion object {
        const val ARG_IS_EDIT = "ARG_IS_EDIT"
        const val ARG_PROJECT_ID = "ARG_PROJECT_ID"
    }

    private val viewModel by viewModel<ProjectViewModel>()

    private lateinit var adapter: ImagesAdapter
    private lateinit var etName: EditText
    private lateinit var etInterval: EditText
    private lateinit var etCount: EditText

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
        etName = et_name
        etInterval = et_interval
        etCount = et_count
        btn_start.setOnClickListener { start() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProject().subscribe(::setProject, ::onError)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.isEdit) {
            menu.add(
                R.id.project_menu_group,
                R.id.project_menu_save,
                Menu.FLAG_ALWAYS_PERFORM_CLOSE,
                R.string.save
            )
        } else {
            menu.add(
                R.id.project_menu_group,
                R.id.project_menu_edit,
                Menu.FLAG_ALWAYS_PERFORM_CLOSE,
                R.string.edit
            )
        }
        menu.add(
            R.id.project_menu_group,
            R.id.project_menu_delete_project,
            Menu.FLAG_ALWAYS_PERFORM_CLOSE,
            R.string.delete_project
        )
        menu.add(
            R.id.project_menu_group, R.id.project_menu_build_gif,
            Menu.FLAG_ALWAYS_PERFORM_CLOSE, R.string.build_gif
        )
        menu.add(
            R.id.project_menu_group, R.id.project_menu_build_video,
            Menu.FLAG_ALWAYS_PERFORM_CLOSE, R.string.build_video
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.project_menu_save -> save()
            R.id.project_menu_edit -> viewModel.isEdit = true
            R.id.project_menu_delete_project -> deleteProject { this.recreate() }
            R.id.project_menu_build_gif -> buildGif()
            R.id.project_menu_build_video -> save()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildGif() {
        save { VideoService.startGif(requireContext(), viewModel.projectId!!) }
    }

    private fun setProject(project: ProjectModel) {
        val interval = project.interval.millisToSeconds().toString()
        val count = project.count.toString()
        etName.setText(project.name)
        etInterval.setText(interval)
        etCount.setText(count)
        adapter.setList(project.images)
    }

    private fun start() = save { TimeLapseService.start(requireContext(), viewModel.projectId) }

    private fun save(function: () -> Unit = {}) = viewModel.save(
        et_name.text.toString(),
        et_interval.text.toString().toInt(),
        et_count.text.toString().toInt(),
        function
    )


    private fun deleteProject(function: () -> Unit = {}) =
        viewModel.deleteProject().subscribe({ function() }, Timber::e)

}
