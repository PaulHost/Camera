package paul.host.lifeidb2b.common.util.databinding

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.util.Pair
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import paul.host.camera.common.util.databinding.BindableString
import paul.host.lifeidb2b.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("bindTo")
    fun bindEditText(view: TextInputEditText, bindableString: BindableString) {
        val pair = view.getTag(R.id.bound_observable) as Pair<*, *>?
        if (pair?.first !== bindableString) {
            view.removeTextChangedListener(pair?.second as TextWatcher?)
            val watcher: TextWatcher = object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int, count: Int
                ) {
                    bindableString.set(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) = Unit

                override fun afterTextChanged(s: Editable) = Unit
            }
            view.setTag(R.id.bound_observable, Pair(bindableString, watcher))
            view.addTextChangedListener(watcher)
        }
        val newValue = bindableString.get()
        if (view.text.toString() != newValue) {
            view.setText(newValue)
        }
    }

    @JvmStatic
    @BindingAdapter("bindTo")
    fun bindAutoCompleteTextView(
        view: AppCompatAutoCompleteTextView,
        bindableString: BindableString
    ) {
        val pair = view.getTag(R.id.bound_observable) as Pair<*, *>?
        if (pair?.first !== bindableString) {
            view.removeTextChangedListener(pair?.second as TextWatcher?)
            val watcher: TextWatcher = object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int, count: Int
                ) {
                    bindableString.set(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) = Unit

                override fun afterTextChanged(s: Editable) = Unit
            }
            view.setTag(R.id.bound_observable, Pair(bindableString, watcher))
            view.addTextChangedListener(watcher)
        }
        val newValue = bindableString.get()
        if (view.text.toString() != newValue) {
            view.setText(newValue)
        }
    }
}

