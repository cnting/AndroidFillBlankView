package com.cnting.fillblankview

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.os.Build
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_fill_blank.view.*

/**
 * Created by cnting on 2019-02-28
 * 填空题
 */
class FillBlankView : RelativeLayout {

    //显示内容
    private var fillContent: String = ""
    //下划线分隔符
    private var fillSplit: String = ""
    private var textColor: Int = 0
    private var textSize: Int = 20
    private var underlineFocusColor: Int = Color.BLACK
    private var underlineUnFocusColor: Int = Color.BLACK
    private var rightAnswerColor: Int = Color.BLACK
    private var isFixedUnderlineWidth: Boolean = true
    private var underlineFixedWidth: Int = 100
    //下划线集合
    private val spanList = mutableListOf<UnderlineSpan>()
    private var lastSpan: UnderlineSpan? = null
    //编辑框矩形
    private var editRectF: RectF? = null
    private var fontTop = 0f
    private var fontBottom = 0f
    private var immFocus: ImmFocus? = null
    private var showRightAnswer = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_fill_blank, this)
        val array = context?.obtainStyledAttributes(attrs, R.styleable.FillBlankView)
        fillContent = array?.getString(R.styleable.FillBlankView_fill_text) ?: ""
        fillSplit = array?.getString(R.styleable.FillBlankView_fill_split) ?: ""
        textColor = array?.getColor(R.styleable.FillBlankView_fill_text_color, textColor) ?: textColor
        textSize = array?.getDimensionPixelSize(R.styleable.FillBlankView_fill_text_size, textSize) ?: textSize
        underlineFocusColor =
            array?.getColor(R.styleable.FillBlankView_underline_focus_color, underlineFocusColor) ?: underlineFocusColor
        underlineUnFocusColor =
            array?.getColor(R.styleable.FillBlankView_underline_unfocus_color, underlineUnFocusColor)
                ?: underlineUnFocusColor
        rightAnswerColor =
            array?.getColor(R.styleable.FillBlankView_right_answer_color, rightAnswerColor) ?: rightAnswerColor
        isFixedUnderlineWidth = array?.getBoolean(R.styleable.FillBlankView_underline_fixed_width, true) ?: true
        underlineFixedWidth =
            array?.getDimensionPixelSize(R.styleable.FillBlankView_underline_fixed_width_size, underlineFixedWidth)
                ?: underlineFixedWidth
        array?.recycle()

        initView()

        doFillBlank()
    }

    private fun initView() {
        fillBlankTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        fillBlankTextView.setTextColor(textColor)
        fillBlankTextView.movementMethod = UnderlineLinkMovementMethod()
    }

    private fun doFillBlank() {
        if (fillSplit.isNotEmpty()) {
            fillContent = FillBlankTagUtil.splitToBlank(fillContent, fillSplit)
        }
        val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(fillContent, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler)
        } else {
            Html.fromHtml(fillContent, imageGetter, tagHandler)
        }

        fillBlankTextView.text = spanned
    }

    private val imageGetter by lazy { FillBlankImageGetter(context) }

    private val tagHandler = Html.TagHandler { opening, tag, output, xmlReader ->
        if (tag.equals(FillBlankTagUtil.TAG_BLANK, true) && opening) {
            val attrs = FillBlankTagUtil.processAttribute(xmlReader)
            val rightAnswers = attrs[FillBlankTagUtil.ATTR_RIGHT_ANSWER]
            val span =
                UnderlineSpan(underlineFocusColor, underlineUnFocusColor, isFixedUnderlineWidth || rightAnswers == null)
            span.setFixedWidth(underlineFixedWidth)
            span.rightAnswers = rightAnswers
            span.clickListener = clickSpanListener
            span.spanText = ""
            span.spanId = spanList.size
            spanList.add(span)
            output.setSpan(span, output.length - 1, output.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            if (showRightAnswer) {
                val spannableString = SpannableString("($rightAnswers)")
                spannableString.setSpan(
                    ForegroundColorSpan(rightAnswerColor),
                    0,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                output.append(spannableString)
            }
        }
    }

    private fun reset() {
        spanList.clear()
    }

    private val clickSpanListener = object : UnderlineSpan.ClickSpanListener {
        override fun onClick(textView: TextView, spanId: Int, span: UnderlineSpan) {
            if (!isEnabled) {
                return
            }
            if (lastSpan != span) {
                fillTextOnLoseFocus()
            }
            fillTextOnNewFocus(span)

            spanList.forEach { it.focusChange(false) }
            span.focusChange(true)
            fillBlankTextView.invalidate()
        }
    }

    private fun fillTextOnNewFocus(span: UnderlineSpan) {
        lastSpan = span
        fillBlankEditText.setText(span.spanText)
        fillBlankEditText.setSelection(span.spanText.length)
        //计算当前EditText应该显示的位置
        val rectF = drawSpanRect(span)
        layoutEditTextPosition(rectF)
    }

    private fun fillTextOnLoseFocus() {
        if (lastSpan != null) {
            lastSpan?.spanText = fillBlankEditText.text.toString()
        }
    }

    private fun drawSpanRect(span: UnderlineSpan): RectF {
        val layout = fillBlankTextView.layout
        val buffer: Spannable = fillBlankTextView.text as Spannable
        val start = buffer.getSpanStart(span)
        val end = buffer.getSpanEnd(span)
        var line = layout.getLineForOffset(start)

        if (editRectF == null) {
            editRectF = RectF()
            val fontMetrics = fillBlankTextView.paint.fontMetrics
            fontTop = fontMetrics.ascent
            fontBottom = fontMetrics.descent
        }
        editRectF?.left = layout.getPrimaryHorizontal(start)
        editRectF?.right = layout.getSecondaryHorizontal(end)
        //通过基线去校准
        line = layout.getLineBaseline(line)
        editRectF?.top = line + fontTop
        editRectF?.bottom = line + fontBottom
        return editRectF!!
    }

    private fun layoutEditTextPosition(rectF: RectF) {
        val layoutParams = fillBlankEditText.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = (rectF.right - rectF.left).toInt()
        layoutParams.height = (rectF.bottom - rectF.top).toInt()
        layoutParams.leftMargin = (fillBlankTextView.left + rectF.left).toInt()
        layoutParams.topMargin = (fillBlankTextView.top + rectF.top).toInt()

        fillBlankEditText.layoutParams = layoutParams
        fillBlankEditText.isFocusable = true
        fillBlankEditText.requestFocus()
        showImm(true, fillBlankEditText)
    }

    private fun showImm(open: Boolean, focusView: View) {
        if (open) {
            ImmFocus.show(open, focusView)
        } else {
            ImmFocus.show(open, null)
        }
    }

    fun setFillContent(content: String, split: String) {
        reset()
        this.fillContent = content
        this.fillSplit = split

        doFillBlank()
    }

    fun showRightAnswer(showRightAnswer: Boolean) {
        this.showRightAnswer = showRightAnswer
        doFillBlank()
    }
}