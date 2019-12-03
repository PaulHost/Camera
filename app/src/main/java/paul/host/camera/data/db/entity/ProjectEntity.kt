package paul.host.camera.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String? = null,
    val name: String,
    val removable: Boolean = false,
    val startTime: Long? = null,
    val interval: Long? = null,
    val count: Int? = null,
    val endTime: Long? = null,
    val exposureTime: Long? = null,
    @Relation(parentColumn = "id", entityColumn = "projectId", entity = ImageEntity::class)
    val images: List<ImageEntity>
)