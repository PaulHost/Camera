package paul.host.camera.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import paul.host.camera.common.util.toImageEntity
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.db.dao.ProjectDao
import paul.host.camera.data.model.ProjectModel
import paul.host.camera.data.model.TimeLapseProjectModel

class ProjectsRepository(private val projectDao: ProjectDao, private val imageDao: ImageDao) {
    fun getProjects(): Flowable<List<TimeLapseProjectModel>> = projectDao.projects()
        .flatMap { entities ->
            Flowable.fromIterable(entities)
                .map { TimeLapseProjectModel(it) }
                .toList()
                .toFlowable()
        }

    fun getProject(id: String): Flowable<ProjectModel> =
        projectDao.project(id).map { TimeLapseProjectModel(it) }

    fun updateProject(project: ProjectModel): Completable = projectDao.update(project.toEntity())

    fun saveProject(project: ProjectModel): Completable =
        projectDao.insert(project.toEntity()).andThen(
            project.images?.toImageEntity(projectId = project.id)?.let { imageDao.insert(it) }
                ?: Completable.complete()
        )

}