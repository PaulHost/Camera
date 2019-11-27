package paul.host.camera.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import paul.host.camera.data.db.Entyties.ProjectEntity
import paul.host.camera.data.db.dao.ProjectDao

@Database(
    entities = [
        ProjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}
