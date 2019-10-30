package paul.host.camera.common

import android.content.Context
import android.os.Environment
import java.io.File


object Constants {

    const val CANCELED = "Canceled"
    const val SUCCESSFUL = "Successful"

    object NAMES {
        const val TIME_LAPSE = "time_lapse_"
    }

    object ACTION {
        val START_FOREGROUND_ACTION = "${this::class.java.canonicalName}.startforeground"
        val STOP_FOREGROUND_ACTION = "${this::class.java.canonicalName}.stopforeground"
    }

    object NOTIFICATION_ID {
        const val TIMELAPSE_SERVICE = 101
        const val VIDEO_MAIKER_SERVICE = 102
    }

    object FOLDERS {
        fun mediaDirFile(context: Context): File = context.externalMediaDirs.first()
        fun externalStorageDirFile(): File = Environment.getExternalStorageDirectory()
    }
}