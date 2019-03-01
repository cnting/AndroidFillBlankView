package com.cnting.fillblankview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Spannable
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ReplacementSpan
import android.widget.TextView

/**
 * Created by cnting on 2019-02-28
 * 用来绘制填空题
 */
class UnderlineSpan(
    private val underlineFocusColor: Int,
    private val underlineUnFocusColor: Int,
    private val isFixedUnderlineWidth: Boolean,
    private val fixedWidth: Int
) : ReplacementSpan() {

    /**
     * 填写的内容
     */
    var spanText = ""
    var spanId: Int = 0
    var underlineWidth = 80
    var clickListener: ClickSpanListener? = null
    private var drawPaint = Paint()
    private val linePaint = Paint()

    init {
        drawPaint.color = underlineUnFocusColor
        linePaint.color = drawPaint.color
        linePaint.strokeWidth = 2f
    }

    /**
     * @return Span替换文字或所占的宽度
     */
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return underlineWidth
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val bottom1 = paint.fontMetrics.bottom
        val y1 = y + bottom1

        val ellipsize =
            TextUtils.ellipsize(
                spanText,
                paint as TextPaint,
                underlineWidth.toFloat(),
                TextUtils.TruncateAt.END
            )  //填写被截断后的内容
        var width = paint.measureText(ellipsize, 0, ellipsize.length)
        width = (underlineWidth - width) / 2

        drawPaint.textSize = paint.textSize
        canvas.drawText(ellipsize, 0, ellipsize.length, x + width, y.toFloat(), drawPaint) //绘制填写的内容

        linePaint.color = drawPaint.color
        canvas.drawLine(x, y1, x + underlineWidth, y1, linePaint)   //绘制下划线
    }

    fun onClick(textView: TextView, buffer: Spannable, isDown: Boolean, x: Int, y: Int, line: Int, off: Int) {
        if (clickListener != null) {
            clickListener?.onClick(textView, spanId, this)
        }
    }

    fun focusChange(onFocus: Boolean) {
        drawPaint.color = if (onFocus) underlineFocusColor else underlineUnFocusColor
    }


    interface ClickSpanListener {
        fun onClick(textView: TextView, spanId: Int, span: UnderlineSpan)
    }
}
