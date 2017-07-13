package touchin.aacplusdbtest.views


import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.TextWatcher
import android.util.AttributeSet

class SafeEditText : AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun addTextChangedListener(watcher: TextWatcher?) {
        if (watcher != null) {
            super.addTextChangedListener(watcher)
        }
    }
}
