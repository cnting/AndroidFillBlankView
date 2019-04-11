package com.cnting.fillblankview

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * 设置点击事件
 */
internal class UnderlineLinkMovementMethod(private val touchSlop: Int) : LinkMovementMethod() {

    var touchX = 0f
    var touchY = 0f

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        if (widget == null) {
            return false
        }
        val action = event?.action
        if (action == MotionEvent.ACTION_DOWN) {
            touchX = event.x
            touchY = event.y
        } else if (action == MotionEvent.ACTION_UP) {
            if (Math.abs(event.x - touchX) < touchSlop && Math.abs(event.y - touchY) < touchSlop) {  //是点击

                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout?.getLineForVertical(y) ?: 0
                val off = layout?.getOffsetForHorizontal(line, x.toFloat()) ?: 0

                val spanArr = buffer?.getSpans(off, off, UnderlineSpan::class.java)
                if (spanArr?.isNotEmpty() == true) {
                    spanArr[0].onClick(widget)
                    return true
                }
            }
        }
        return false
    }
}