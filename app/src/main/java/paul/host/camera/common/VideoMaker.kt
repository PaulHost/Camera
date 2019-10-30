package paul.host.camera.common

import io.microshow.rxffmpeg.RxFFmpegCommandList
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegProgress
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import io.reactivex.Flowable
import timber.log.Timber


object VideoMaker {
    val template =
        "ffmpeg -r 1/5 -i img%03d.png -c:v libx264 -vf fps=25 -pix_fmt yuv420p out.mp4".toFfmpgCommand()

    fun make(command: Array<String>): Flowable<RxFFmpegProgress> =
        RxFFmpegInvoke.getInstance().runCommandRxJava(command)

    fun simpleSubscriber() = object : RxFFmpegSubscriber() {
        override fun onFinish() {
            Timber.d("Successful")
        }

        override fun onProgress(progress: Int, progressTime: Long) {
        }

        override fun onCancel() {
            Timber.d("Cancelled")
        }

        override fun onError(message: String) {
            Timber.d("onError：$message")
        }
    }

    fun command(
        iName: String,
        fps: Int = 25,
        vName: String
    ): Array<String> = RxFFmpegCommandList().let {
        it.append("-r").append("1/5")
        it.append("-i").append("$iName%3d")
        it.append("-c:v").append("libx264")
        it.append("-vf").append("fps=$fps")
        it.append("-pix_fmt").append("yuv420p")
        it.append("$vName.mp4")
        it.build()
    }

    fun String.toFfmpgCommand(): Array<String> =
        this.split(" ".toRegex()).dropLastWhile { this.isEmpty() }.toTypedArray()

}