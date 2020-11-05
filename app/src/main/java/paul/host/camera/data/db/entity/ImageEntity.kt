package paul.host.camera.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import paul.host.camera.common.Constants
import kotlin.random.Random

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE),
    val name: String = Constants.EMPTY_STRING,
    val projectId: String = Constants.EMPTY_STRING,
    val path: String = Constants.EMPTY_STRING
)