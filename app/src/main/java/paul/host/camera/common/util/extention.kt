package paul.host.camera.common.util

fun Int.toImageName(): String = when {
    this < 10 -> "00$this"
    this < 100 -> "0$this"
    else -> "$this"
}