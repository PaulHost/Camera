package paul.host.camera.common.gif

import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
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