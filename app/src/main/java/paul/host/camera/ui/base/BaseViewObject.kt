package paul.host.camera.ui.base

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

import org.parceler.Parcel
import org.parceler.Transient

@Parcel(Parcel.Serialization.BEAN)
open class BaseViewObject : BaseObservable() {

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    @Synchronized
    override fun addOnPropertyChangedCallback(listener: Observable.OnPropertyChangedCallback) {
        if (this.mCallbacks == null) {
            this.mCallbacks = PropertyChangeRegistry()
        }

        this.mCallbacks!!.add(listener)
    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(listener: Observable.OnPropertyChangedCallback) {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.remove(listener)
        }
    }

    @Synchronized
    override fun notifyChange() {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.notifyCallbacks(this, 0, null)
        }
    }

    override fun notifyPropertyChanged(fieldId: Int) {
        if (this.mCallbacks != null) {
            this.mCallbacks!!.notifyCallbacks(this, fieldId, null)
        }
    }

    fun notifyChangeFunction(function: () -> Unit) {
        function()
        notifyChange()
    }
}