package paul.host.camera.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import paul.host.camera.data.db.dao.ProjectDao
import paul.host.camera.data.db.entity.ProjectEntity
import paul.host.camera.data.model.ImageModel
import paul.host.camera.data.model.ProjectModel
import java.io.File

class ProjectsRepository(
    private val projectDao: ProjectDao,
    private val imageRepository: ImageRepository
) {
    fun getProjects(): Flowable<List<ProjectModel>> = projectDao.projects()
        .flatMap { entities ->
            Flowable.fromIterable(entities)
                .flatMap {
                    Flowable.just(it)
                        .zipWith(imageRepository.getImages(it.id), createProjectModel())
                }
                .toList()
                .toFlowable()
        }

    fun getProject(id: String): Flowable<ProjectModel> =
        Flowable.zip(
            projectDao.project(id), imageRepository.getImages(id), createProjectModel()
        )

    fun saveProject(project: ProjectModel): Completable = projectDao.insert(project.toEntity())
        .andThen(imageRepository.saveImages(project.images))

    private fun createProjectModel(): BiFunction<ProjectEntity, List<ImageModel>, ProjectModel> =
        BiFunction { project, images -> ProjectModel(project, images) }

    fun deleteProject(project: ProjectModel): Completable = Completable.concatArray(
        projectDao.delete(project.toEntity()),
        imageRepository.deleteImages(project.images),
        Completable.fromAction {
            project.images.forEach {
                File(it.path).delete()
            }
        }
    )

}
