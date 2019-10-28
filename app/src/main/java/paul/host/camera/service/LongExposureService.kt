package paul.host.camera.service

import android.content.Intent
import paul.host.camera.ui.ShotActivity


class LongExposureService : TimeLapseService(LongExposureService::class.java.simpleName) {
    override fun takeShotIntent(): Intent = super.takeShotIntent().apply {
        putExtra(ShotActivity.EXTRA_LONG_EXPOSURE_SHOT, true)
    }
}