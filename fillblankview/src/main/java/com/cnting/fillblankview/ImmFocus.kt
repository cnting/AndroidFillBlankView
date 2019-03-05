package com.cnting.fillblankview

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

// 处理焦点
object ImmFocus {
    // 显示/隐藏
    fun show(bOn: Boolean, focus: View?): Boolean {
        if (focus == null) {
            return false
        }
        val imm = focus.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return if (bOn) {
            focus.requestFocus()
            imm.showSoftInput(focus, 0)
        } else {
            focus.clearFocus()
            imm.hideSoftInputFromWindow(focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
