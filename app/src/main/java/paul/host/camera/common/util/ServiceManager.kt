@file:Suppress("unused")

package paul.host.camera.common.util

import android.content.Context
import android.content.Intent
import android.os.Build
import paul.host.camera.common.Constants
import timber.log.Timber

object ServiceManager {
    private val SERVICES: MutableMap<String, ServiceWrapper> = mutableMapOf()

    fun start(context: Context, service: Class<*>) = service.simpleName.let {
        SERVICES[it]?.start() ?: SERVICES.put(
            it,
            ServiceWrapper(context, service)
        )
        this
    }

    fun start(context: Context, serviceList: List<Class<*>>) {
        serviceList.forEach {
            start(context, it)
        }
    }

    fun stop(service: Class<*>) = service.simpleName.let {
        SERVICES[it]?.stop()
        SERVICES.remove(it)
        this
    }

    fun stop(serviceList: List<Class<*>>) {
        serviceList.forEach {
            stop(it)
        }
    }

    fun stopAll() {
        SERVICES.values.forEach {
            stop(it.service)
        }
    }

    private class ServiceWrapper(val context: Context, val service: Class<*>) {
        var isServiceBound = false

        var intent = Intent(context, service)

        init {
            start()
        }

        fun start() {
            Timber.d("MY_LOG: start")
            if (!isServiceBound) {
                intent.action = Constants.ACTION.START_FOREGROUND_ACTION
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
                isServiceBound = true
            }
        }

        fun stop() {
            Timber.d("MY_LOG: stop")
            if (isServiceBound) {
                intent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
                context.stopService(intent)
                isServiceBound = false
            }
        }
    }
}