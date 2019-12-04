package paul.host.camera.data.model

import paul.host.camera.common.Constants
import paul.host.camera.data.db.entity.ImageEntity
import paul.host.camera.data.db.entity.ProjectEntity
import java.util.*

class DelaydTimeLapseProjectModel(
    id: String,
    name: String,
    images: List<String>,
    var removable: Boolean,
    var startTime: Long,
    var interval: Long,
    var endTime: Long,
    var exposureTime: Long
) : ProjectModel(id, name, images) {

    constructor(projectEntity: ProjectEntity? = null, images: List<ImageEntity>? = null) : this(
        id = projectEntity?.id ?: UUID.randomUUID().toString(),
        name = projectEntity?.name ?: Constants.EMPTY_STRING,
        removable = projectEntity?.removable ?: false,
        startTime = projectEntity?.startTime ?: 0L,
        interval = projectEntity?.interval ?: 0L,
        endTime = projectEntity?.endTime ?: 0L,
        exposureTime = projectEntity?.exposureTime ?: 0L,
        images = images?.map { it.path } ?: mutableListOf()
    )

    override fun toEntity() = ProjectEntity(
        id = id,
        name = name,
        removable = removable,
        startTime = startTime,
        interval = interval,
        endTime = endTime,
        exposureTime = exposureTime
    )

}
