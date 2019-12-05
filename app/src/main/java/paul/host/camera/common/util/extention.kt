package paul.host.camera.common.util

fun Int.toImageName(): String = when {
    this < 10 -> "00$this"
    this < 100 -> "0$this"
    else -> "$this"
}

fun Int.toImageName(maxCount: Int): String {
    val name = this.toString()
    val zeros = (maxCount.toString().length - name.length).let {
        var result = ""
        for (i in 0..it) result += "0"
        result
    }

    return "$zeros$name"
}

fun Long.millisToSeconds(): Int = (this / 1000).toInt()

fun Int.secToMillis(): Long = this * 1000L

