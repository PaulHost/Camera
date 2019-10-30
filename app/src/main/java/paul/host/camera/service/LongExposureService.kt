package paul.host.camera.service

import android.content.Intent
import paul.host.camera.common.Constants
import paul.host.camera.common.util.toImageName
import paul.host.camera.ui.ShotActivity


class LongExposureService : TimeLapseService(LongExposureService::class.java.simpleName) {
    var count: Int = 0
    override fun takeShotIntent(): Intent = super.takeShotIntent().apply {
        putExtra(ShotActivity.EXTRA_LONG_EXPOSURE_SHOT, true)
        putExtra(
            ShotActivity.EXTRA_PICTURE_NAME,
            "${Constants.NAMES.TIME_LAPSE}${count.toImageName()}"
        )
        count++
    }
}