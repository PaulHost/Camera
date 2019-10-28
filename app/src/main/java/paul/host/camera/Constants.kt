package paul.host.camera


object Constants {
    object ACTION {
        val START_FOREGROUND_ACTION = "${this::class.java.canonicalName}.startforeground"
        val STOP_FOREGROUND_ACTION = "${this::class.java.canonicalName}.stopforeground"
    }

    object NOTIFICATION_ID {
        const val FOREGROUND_SERVICE = 101
    }
}