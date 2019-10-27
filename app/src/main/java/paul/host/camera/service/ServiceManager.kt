@file:Suppress("unused")

package paul.host.camera.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

object ServiceManager {
    private val services: MutableMap<String, Binder> = mutableMapOf()

    fun bind(context: Context, service: Class<*>) = service.simpleName.let {
        services[it]?.bind() ?: services.put(it, Binder(context, service))
        this
    }

    fun bind(context: Context, serviceList: List<Class<*>>) {
        serviceList.forEach {
            bind(context, it)
        }
    }

    fun unbind(service: Class<*>) = service.simpleName.let {
        services[it]?.unbind()
        services.remove(it)
        this
    }

    fun unbind(serviceList: List<Class<*>>) {
        serviceList.forEach {
            unbind(it)
        }
    }

    fun unbindAll() {
        services.values.forEach {
            unbind(it.service)
        }
    }

    private class Binder(val context: Context, val service: Class<*>) {
        var isServiceBound = false

        private val serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                isServiceBound = true
                Log.d(service.javaClass.simpleName, "MY_LOG: connected")

            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                isServiceBound = false
                Log.d(service.simpleName, "MY_LOG: disconnected")
            }
        }

        init {
            bind()
        }

        fun bind() {
            Log.d(service.simpleName, "MY_LOG: binding")
            if (!isServiceBound) context.bindService(
                Intent(context, service),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
            isServiceBound = true
        }

        fun unbind() {
            Log.d(service.simpleName, "MY_LOG: unbinding")
            if (isServiceBound) context.unbindService(serviceConnection)
        }

    }
}