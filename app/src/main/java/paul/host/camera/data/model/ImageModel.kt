package paul.host.camera.data.model

import paul.host.camera.common.Constants
import paul.host.camera.data.db.entity.ImageEntity

data class ImageModel(
    val id: Int? = null,
    val name: String = Constants.EMPTY_STRING,
    val projectId: String = Constants.EMPTY_STRING,
    val path: String = Constants.EMPTY_STRING
)

fun ImageEntity.toModel(): ImageModel = ImageModel(id, name, projectId, path)

fun ImageModel.toEntity(): ImageEntity = ImageEntity(id, name, projectId, path)

fun List<ImageModel>.toImageEntity(): List<ImageEntity> = map { it.toEntity() }

fun List<ImageEntity>.toImageModel(): List<ImageModel> = map { it.toModel() }
