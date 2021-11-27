package paul.host.camera.common.util.databinding

import org.parceler.Parcel
import paul.host.camera.common.Constants
import paul.host.camera.ui.base.BaseViewObject

@Parcel(analyze = [String::class])
class BindableString() : BaseViewObject() {
    private var value: String = Constants.EMPTY_STRING
    private val onPropertyChanged = mutableListOf<(String) -> Unit>()

    constructor(value: String) : this() {
        this.value = value
    }

    constructor(onPropertyChanged: (String) -> Unit) : this() {
        this.onPropertyChanged.add(onPropertyChanged)
    }

    constructor(value: String, onPropertyChanged: (String) -> Unit) : this(value) {
        this.onPropertyChanged.add(onPropertyChanged)
    }

    fun onPropertyChanged(onPropertyChanged: (String) -> Unit) {
        this.onPropertyChanged.add(onPropertyChanged)
    }

    fun get(): String = value

    fun set(value: String?) = value?.let { _ ->
        this.value = value
        notifyChange()
        onPropertyChanged.forEach { it(value) }
    } ?: Unit

    fun isEmpty(): Boolean = value.isEmpty()

}
