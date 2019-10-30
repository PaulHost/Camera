package paul.host.camera.common

import android.content.Context
import android.os.Environment
import java.io.File


object Constants {

    object NAMES {
        const val IMAGE = "time_lapse_"
    }

    object ACTION {
        val START_FOREGROUND_ACTION = "${this::class.java.canonicalName}.startforeground"
        val STOP_FOREGROUND_ACTION = "${this::class.java.canonicalName}.stopforeground"
    }

    object NOTIFICATION_ID {
        const val FOREGROUND_SERVICE = 101
    }

    object FOLDERS {
        fun mediaDirFile(context: Context): File = context.externalMediaDirs.first()
        fun externalStorageDirFile(): File = Environment.getExternalStorageDirectory()
    }
}