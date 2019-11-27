@file:Suppress("unused")

package paul.host.camera.data.db.dao

import androidx.room.*
import io.reactivex.Flowable
import paul.host.camera.data.db.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun projects(): Flowable<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun project(id: String): Flowable<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(projects: List<ProjectEntity>)

    @Update
    fun update(project: ProjectEntity)

    @Update
    fun update(projects: List<ProjectEntity>)

    @Delete
    fun delete(project: ProjectEntity)
}
