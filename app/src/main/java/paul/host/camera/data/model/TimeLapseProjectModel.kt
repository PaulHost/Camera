package paul.host.camera.data.model

import paul.host.camera.common.Constants
import paul.host.camera.common.util.toImageName
import paul.host.camera.data.db.entity.ImageEntity
import paul.host.camera.data.db.entity.ProjectEntity
import java.util.*

class TimeLapseProjectModel(
    id: String,
    name: String,
    images: List<String>,
    var removable: Boolean,
    var startTime: Long,
    var interval: Long,
    var endTime: Long,
    var exposureTime: Long
) : ProjectModel(id, name, images) {

    constructor(projectEntity: ProjectEntity? = null) : this(
        id = projectEntity?.id ?: UUID.randomUUID().toString(),
        name = projectEntity?.name ?: Constants.EMPTY_STRING,
        removable = projectEntity?.removable ?: false,
        startTime = projectEntity?.startTime ?: 0L,
        interval = projectEntity?.interval ?: 0L,
        endTime = projectEntity?.endTime ?: 0L,
        exposureTime = projectEntity?.exposureTime ?: 0L,
        images = projectEntity?.images?.map { it.path } ?: mutableListOf()
    )

    fun toEntity() = ProjectEntity(
        id = id,
        name = name,
        removable = removable,
        startTime = startTime,
        interval = interval,
        endTime = endTime,
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
