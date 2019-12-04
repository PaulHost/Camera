package paul.host.camera.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import paul.host.camera.common.Constants

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String = Constants.EMPTY_STRING,
    val projectId: String = Constants.EMPTY_STRING,
    val path: String = Constants.EMPTY_STRING
)