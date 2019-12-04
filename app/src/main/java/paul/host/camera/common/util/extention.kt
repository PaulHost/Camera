package paul.host.camera.common.util

import paul.host.camera.common.Constants
import paul.host.camera.data.db.entity.ImageEntity

fun Int.toImageName(): String = when {
    this < 10 -> "00$this"
    this < 100 -> "0$this"
    else -> "$this"
}

fun Int.toImageName(maxCount: Int): String {
    val name = this.toString()
    val zeros = (maxCount.toString().length - name.length).let {
        var result = ""
        for (i in 0..it) result += "0"
        result
    }

    return "$zeros$name"
}

fun List<String>.toImageEntity(
    projectId: String,
    name: String = Constants.EMPTY_STRING
): List<ImageEntity> {
    val entities = mutableListOf<ImageEntity>()
    this.forEachIndexed { i, path ->
        entities.add(
            ImageEntity(
                projectId = projectId,
                name = "${name}_${i.toImageName(this.size)}",
                path = path
            )
        )
    }
    return entities
}

