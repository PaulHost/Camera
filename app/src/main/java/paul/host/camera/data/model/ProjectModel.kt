package paul.host.camera.data.model

import paul.host.camera.common.Constants
import paul.host.camera.data.db.entity.ProjectEntity
import java.util.*

class ProjectModel(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var removable: Boolean = true,
    var startTime: Long = Constants.ZERO_LONG,
    var interval: Long = Constants.ZERO_LONG,
    var count: Int = Constants.ZERO,
    var endTime: Long = Constants.ZERO_LONG,
    var exposureTime: Long = Constants.ZERO_LONG,
    val images: List<ImageModel> = mutableListOf(),
    val status: Status = Status.EDITABLE
) {
    constructor(projectEntity: ProjectEntity? = null, images: List<ImageModel>? = null) : this(
        id = projectEntity?.id ?: UUID.randomUUID().toString(),
        name = projectEntity?.name ?: Constants.EMPTY_STRING,
        removable = projectEntity?.removable ?: false,
        startTime = projectEntity?.startTime ?: 0L,
        interval = projectEntity?.interval ?: 0L,
        count = projectEntity?.count ?: Constants.ZERO,
        endTime = projectEntity?.endTime ?: 0L,
        exposureTime = projectEntity?.exposureTime ?: 0L,
        images = images ?: mutableListOf(),
        status = projectEntity?.status ?: Status.EDITABLE
    )

    constructor(project: ProjectModel) : this(
        id = project.id,
        name = project.name,
        removable = project.removable,
        startTime = startTime(project),
        interval = interval(project),
        count = count(project),
        endTime = endTime(project),
        exposureTime = project.exposureTime,
        images = project.images,
        status = project.status
    )

    fun toEntity() = ProjectEntity(
        id = id,
        name = name,
        removable = removable,
        startTime = startTime,
        interval = interval,
        count = count,
        endTime = endTime,
        exposureTime = exposureTime
    )
}

private fun startTime(project: ProjectModel) =
    if (project.startTime == Constants.ZERO_LONG) System.currentTimeMillis() else project.startTime

private fun count(project: ProjectModel): Int = if (
    project.count == Constants.ZERO
    && project.endTime != Constants.ZERO_LONG
    && project.interval != Constants.ZERO_LONG
) ((project.endTime - if (project.startTime != Constants.ZERO_LONG) project.startTime else System.currentTimeMillis())
        / project.interval).toInt()
else project.count

private fun interval(project: ProjectModel): Long = if (
    project.interval == Constants.ZERO_LONG
    && project.endTime != Constants.ZERO_LONG
    && project.count != Constants.ZERO
    && project.startTime != Constants.ZERO_LONG
) (project.endTime - project.startTime) / project.count else project.interval

private fun endTime(project: ProjectModel): Long = if (
    project.endTime == Constants.ZERO_LONG
    && project.interval != Constants.ZERO_LONG
    && project.count != Constants.ZERO
) (project.interval * project.count) + if (project.startTime != Constants.ZERO_LONG) project.startTime else System.currentTimeMillis()
else project.interval
