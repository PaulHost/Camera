package paul.host.camera.common

import android.content.Context
import android.os.Environment
import java.io.File


object Constants {

    const val CANCELED = "Canceled"
    const val SUCCESSFUL = "Successful"
    const val CHANNEL_ID = "1488"

    object NAMES {
        const val TIME_LAPSE = "time_lapse_"
    }

    object ACTION {
        val START_FOREGROUND_ACTION = "${this::class.java.`package`}.startforeground"
        val STOP_FOREGROUND_ACTION = "${this::class.java.`package`}.stopforeground"
    }

    object NOTIFICATION_ID {
        const val TIMELAPSE_SERVICE = 101
        const val VIDEO_MAKER_SERVICE = 102
    }

    object FOLDERS {
        fun mediaDirFile(context: Context): File = context.externalMediaDirs.first()
        fun externalStorageDirFile(): File = Environment.getExternalStorageDirectory()
    }
}