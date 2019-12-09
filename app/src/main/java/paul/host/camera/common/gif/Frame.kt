package paul.host.camera.common.gif

import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.get


data class Frame(
    /**
     * Resolution of the frame in pixels (before rotation).
     */
    val size: Size,
    /**
     * Image in 2D RGB format.
     */
    val image: Array<IntArray>
) {

    companion object {
        fun create(bitmap: Bitmap) = Frame(
            size = Size(bitmap.width, bitmap.height),
            image = bitmap.to2DRGB()
        )
    }


    override fun toString(): String {
        return "Frame{" +
                "size=$size" +
                ", image=[${image.size}][${image.first().size}]" +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Frame

        if (size != other.size) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }

}

fun Bitmap.to2DRGB(): Array<IntArray> {
    val width: Int = this.width
    val height: Int = this.height
    val result = Array(height) { IntArray(width) }
    for (row in 0 until height) {
        for (col in 0 until width) {
            result[row][col] = this[col, row]
        }
    }
    return result
}