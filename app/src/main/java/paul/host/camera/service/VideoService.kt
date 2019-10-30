package paul.host.camera.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class VideoService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("MY_LOG: onBind")
        return null
    }


}