@file:Suppress("unused")

package paul.host.camera.data.db.dao


import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import paul.host.camera.data.db.entity.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM images")
    fun images(): Flowable<List<ImageEntity>>

    @Query("SELECT * FROM images  WHERE projectId = :projectId")
    fun images(projectId: String): Flowable<List<ImageEntity>>

    @Query("SELECT * FROM images WHERE id = :id")
    fun image(id: Int): Flowable<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images: List<ImageEntity>): Completable

    @Update
    fun update(image: ImageEntity): Completable

    @Update
    fun update(images: List<ImageEntity>): Completable

    @Delete
    fun delete(image: ImageEntity): Completable

    @Delete
    fun delete(image: List<ImageEntity>): Completable
}
