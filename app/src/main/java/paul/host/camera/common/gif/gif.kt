package paul.host.camera.common.gif

import android.graphics.Bitmap
import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import io.reactivex.CompletableObserver
import io.reactivex.CompletableSource
import io.reactivex.disposables.Disposable
import paul.host.camera.common.Constants
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream


class Gif(private val frames: List<Frame>) {
    fun save(path: String, progress: (Int) -> Unit = {}) {
        val outputStream: OutputStream = FileOutputStream(path)
        val options = ImageOptions()
        GifEncoder(
            outputStream,
            frames.first().size.width,
            frames.first().size.height,
            0
        ).apply {
            frames.forEachIndexed { i, frame ->
                addImage(frame.image, options)
                progress((i / frames.size) * 100)
            }
        }.finishEncoding()
        outputStream.close()
    }
}

class RxGif(name: String = System.currentTimeMillis().toString()) : CompletableSource, Disposable {
    private val path: String =
        Constants.FOLDERS.externalStorageDirFile().absolutePath + "/$name.gif"
    private var outputStream: OutputStream? = null
    private var gifEncoder: GifEncoder? = null
    private val options = ImageOptions()

    init {
        outputStream = FileOutputStream(path)
    }

    fun onNext(bitmap: Bitmap): RxGif {
        Timber.d("MY_LOG: onNext")
        gifEncoder(bitmap).addImage(bitmap.to2DRGB(), options)
        return this
    }

    override fun subscribe(co: CompletableObserver) {
        Timber.d("MY_LOG: subscribe")
        co.onSubscribe(this)
        if (File(path).exists()) {
            co.onComplete()
        } else {
            co.onError(FileNotFoundException("File is not created"))
        }
        dispose()
    }

    private fun gifEncoder(bitmap: Bitmap): GifEncoder {
        gifEncoder = if (gifEncoder == null) {
            GifEncoder(
                outputStream,
                bitmap.width,
                bitmap.height,
                0
            )
        } else gifEncoder

        return gifEncoder!!
    }

    override fun isDisposed(): Boolean = gifEncoder == null && outputStream == null

    override fun dispose() {
        Timber.d("MY_LOG: dispose")
        gifEncoder?.finishEncoding()
        gifEncoder = null
        outputStream?.close()
        outputStream = null
    }

}