package paul.host.camera.data.db.Entyties

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val name: String,
    val projectId: String,
    val path: String
)