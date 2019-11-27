package paul.host.camera.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import paul.host.camera.data.db.entity.ImageEntity
import paul.host.camera.data.db.entity.ProjectEntity
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.db.dao.ProjectDao

@Database(
    entities = [
        ProjectEntity::class,
        ImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun imageDao(): ImageDao
}
