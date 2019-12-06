package paul.host.camera.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import paul.host.camera.data.db.dao.ImageDao
import paul.host.camera.data.model.*

class ImageRepository(private val imageDao: ImageDao) {
    fun getImages(projectId: String): Flowable<List<ImageModel>> =
        imageDao.images(projectId).map { it.toImageModel() }

    fun saveImages(list: List<ImageModel>): Completable = imageDao.insert(list.toImageEntity())

    fun saveImage(image: ImageModel): Completable = imageDao.insert(image.toEntity())

    fun getImage(id: Int): Flowable<ImageModel> = imageDao.image(id).map { it.toModel() }

    fun deleteImages(images: List<ImageModel>) = imageDao.delete(images.toImageEntity())

}