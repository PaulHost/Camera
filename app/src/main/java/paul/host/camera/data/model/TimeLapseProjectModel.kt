package paul.host.camera.data.model

import paul.host.camera.common.Constants
import paul.host.camera.common.util.toImageName
import paul.host.camera.data.db.entity.ImageEntity
import paul.host.camera.data.db.entity.ProjectEntity
import java.util.*

class TimeLapseProjectModel(
    id: String,
    name: String,
    images: List<String> = listOf(),
    var removable: Boolean = false,
    var interval: Long,
    var count: Int,
    var exposureTime: Long = 0L
) : ProjectModel(id, name, images) {

    constructor(projectEntity: ProjectEntity? = null) : this(
        id = projectEntity?.id ?: UUID.randomUUID().toString(),
        name = projectEntity?.name ?: Constants.EMPTY_STRING,
        removable = projectEntity?.removable ?: false,
        interval = projectEntity?.interval ?: 0L,
        count = projectEntity?.count ?: 0,
        exposureTime = projectEntity?.exposureTime ?: 0L,
        images = projectEntity?.images?.map { it.path } ?: mutableListOf()
    )

    override fun toEntity() = ProjectEntity(
        id = id,
        name = name,
        removable = removable,
        interval = interval,
        count = count,
        exposureTime = exposureTime,
        images = images.toImageEntity()
    )

    private fun List<String>.toImageEntity(): List<ImageEntity> {
        val entities = mutableListOf<ImageEntity>()
        this.forEachIndexed { i, path ->
            entities.add(
                ImageEntity(
                    projectId = id,
                    name = "${name}_${i.toImageName(this.size)}",
                    path = path
                )
            )
        }
        return entities
    }

}
