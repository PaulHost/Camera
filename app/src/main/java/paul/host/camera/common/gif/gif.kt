package paul.host.camera.common.gif

import android.graphics.Bitmap
import com.jakewharton.rx.ReplayingShare
import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import io.reactivex.*
import paul.host.camera.common.Constants
import paul.host.camera.common.util.rx.fromIoToMainThread
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

class RxGif(name: String = System.currentTimeMillis().toString()) : CompletableSource {
    private val path: String = Constants.FOLDERS.externalStorageDirFile().absolutePath + "/$name"
    private var outputStream: OutputStream? = null
    private var gifEncoder: GifEncoder? = null
    private val options = ImageOptions()
    private var emitter: FlowableEmitter<Bitmap>? = null
    private val flowable: Flowable<Bitmap> = Flowable.create(
        FlowableOnSubscribe<Bitmap> {
            emitter = it
            outputStream = FileOutputStream(path)
        },
        BackpressureStrategy.LATEST
    )
        .doOnNext {
            gifEncoder?.addImage(it.to2DRGB(), options)
        }
        .doFinally {
            gifEncoder?.finishEncoding()
            outputStream?.close()
        }
        .compose(ReplayingShare.instance())
        .fromIoToMainThread()

    fun next(bitmap: Bitmap): RxGif {
        emitter?.onNext(bitmap)
        return this
    }

    override fun subscribe(co: CompletableObserver) {
        flowable.flatMapCompletable { Completable.complete() }.subscribe(co)
    }

}