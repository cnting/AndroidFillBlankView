package com.cnting.android_fillblankview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import com.cnting.fillblankview.FillBlankTagUtil
import com.cnting.fillblankview.FillBlankView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        init2()
        init3()
        init4()

        fab.setOnClickListener {
            fillBlankView1.autoNextBlank()  //点击自动跳转下一个空
//            val stringBuilder = StringBuilder()
//            stringBuilder.append("1:\n")
//            stringBuilder.append(fillBlankView1.getUserAnswers().joinToString(",") + "\n")
//            stringBuilder.append("2:\n")
//            stringBuilder.append(fillBlankView2.getUserAnswers().joinToString(",") + "\n")
//            stringBuilder.append("3:\n")
//            stringBuilder.append(fillBlankView3.getUserAnswers().joinToString(",") + "\n")
//            stringBuilder.append("4:\n")
//            stringBuilder.append(fillBlankView4.getUserAnswers().joinToString(",") + "\n")
//            Log.d("FillBlank", stringBuilder.toString())
        }
    }

    private fun init2() {
        val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep"))
        val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves"))
        val content =
            "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
        fillBlankView2.setFillContent(content, "")
        fillBlankView2.setOnBlankFocusChangeListener(object : FillBlankView.OnBlankFocusChangeListener {
            override fun onLoseFocus(index: Int, spanText: String?) {
                Log.d("onLoseFocus", "index:$index,spanText:$spanText")
            }
        })
    }

    private fun init3() {
        val image = FillBlankTagUtil.imageToHtml(R.mipmap.ic_launcher)
        val content =
            "A group of ____ are eating grass and ____ (leaf) in front of the farm.$image"
        fillBlankView3.setFillContent(content, "____")
    }

    private fun init4() {
        val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep", "a sheep"), "sheep")
        val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves", "leaffff"), "leafs", false)
        val content =
            "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
        fillBlankView4.setFillContent(content, "")
//        fillBlankView4.showUserAnswer(false)
        fillBlankView4.showAnswerResult(true)
        fillBlankView4.isEnabled = false
    }

}
