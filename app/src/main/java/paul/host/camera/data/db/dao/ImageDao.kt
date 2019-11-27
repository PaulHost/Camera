@file:Suppress("unused")

package paul.host.camera.data.db.dao


import androidx.room.*
import io.reactivex.Flowable
import paul.host.camera.data.db.entity.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM images")
    fun images(): Flowable<List<ImageEntity>>

    @Query("SELECT * FROM images  WHERE projectId = :projectId")
    fun images(projectId: String): Flowable<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE id = :id")
    fun image(id: String): Flowable<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images: List<ImageEntity>)

    @Update
    fun update(image: ImageEntity)

    @Update
    fun update(images: List<ImageEntity>)

    @Delete
    fun delete(image: ImageEntity)
}
