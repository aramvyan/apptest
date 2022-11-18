package am.testapp.shared

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun View.setSingleOnClickListener(crossinline onClick: (View) -> Unit) {
    setOnClickListener {
        forWhileDisable()
        onClick(it)
    }
}

fun View.forWhileDisable() {
    CoroutineScope(Dispatchers.Main).launch {
        this@forWhileDisable.isEnabled = false
        delay(500)
        this@forWhileDisable.isEnabled = true
    }
}