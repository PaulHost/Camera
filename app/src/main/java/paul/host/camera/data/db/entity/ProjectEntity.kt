package paul.host.camera.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import paul.host.camera.data.model.Status

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val removable: Boolean = false,
    val startTime: Long? = null,
    val interval: Long? = null,
    val count: Int? = null,
    val endTime: Long? = null,
    val exposureTime: Long? = null,
    val status: Status = Status.EDITABLE
)