@file:Suppress("unused")

package paul.host.camera.util

import android.content.Context
import android.content.Intent
import android.util.Log
import paul.host.camera.Constants

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
            Log.d(service.simpleName, "MY_LOG: start")
            if (!isServiceBound) {
                intent.action = Constants.ACTION.START_FOREGROUND_ACTION
                context.startService(intent)
                isServiceBound = true
            }
        }

        fun stop() {
            Log.d(service.simpleName, "MY_LOG: stop")
            if (isServiceBound) {
                intent.action = Constants.ACTION.STOP_FOREGROUND_ACTION
                context.stopService(intent)
                isServiceBound = false
            }
        }
    }
}