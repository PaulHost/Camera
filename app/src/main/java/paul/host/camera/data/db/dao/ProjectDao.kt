@file:Suppress("unused")

package paul.host.camera.data.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import paul.host.camera.data.db.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun projects(): Flowable<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun project(id: String): Flowable<ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: ProjectEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(projects: List<ProjectEntity>): Completable

    @Update
    fun update(project: ProjectEntity): Completable

    @Update
    fun update(projects: List<ProjectEntity>): Completable

    @Delete
    fun delete(project: ProjectEntity): Completable
}
