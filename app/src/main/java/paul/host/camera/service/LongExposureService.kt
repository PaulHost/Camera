package paul.host.camera.service

import android.app.IntentService
import android.content.Intent
import paul.host.camera.ui.LongExposureActivity

class LongExposureService: IntentService(LongExposureService::class.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Thread.sleep(1000)
        startActivity(LongExposureActivity.getIntent(applicationContext))
    }

}