package paul.host.camera.data.model

import paul.host.camera.data.db.entity.ProjectEntity

abstract class ProjectModel(
    val id: String,
    var name: String,
    val images: List<String>?
) {
    abstract fun toEntity(): ProjectEntity
}