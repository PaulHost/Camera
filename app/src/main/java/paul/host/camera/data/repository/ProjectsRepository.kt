package paul.host.camera.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import paul.host.camera.common.util.toImageEntity
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.db.dao.ProjectDao
import paul.host.camera.data.db.entity.ImageEntity
import paul.host.camera.data.db.entity.ProjectEntity
import paul.host.camera.data.model.ProjectModel

class ProjectsRepository(private val projectDao: ProjectDao, private val imageDao: ImageDao) {
    fun getProjects(): Flowable<List<ProjectModel>> = projectDao.projects()
        .flatMap { entities ->
            Flowable.fromIterable(entities)
                .flatMap { Flowable.just(it).zipWith(imageDao.images(it.id), createProjectModel()) }
                .toList()
                .toFlowable()
        }

    fun getProject(id: String): Flowable<ProjectModel> =
        Flowable.zip(
            projectDao.project(id), imageDao.images(id), createProjectModel()
        )

    fun updateProject(project: ProjectModel): Completable = projectDao.update(project.toEntity())

    fun saveProject(project: ProjectModel): Completable =
        projectDao.insert(project.toEntity()).andThen(
            project.images?.toImageEntity(projectId = project.id)?.let { imageDao.insert(it) }
                ?: Completable.complete()
        )

    private fun createProjectModel(): BiFunction<ProjectEntity, List<ImageEntity>, ProjectModel> =
        BiFunction { project, images -> ProjectModel(project, images) }
}