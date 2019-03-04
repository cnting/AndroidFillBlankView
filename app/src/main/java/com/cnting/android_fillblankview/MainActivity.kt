package com.cnting.android_fillblankview

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.cnting.fillblankview.FillBlankTagUtil

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

    }

    private fun init2() {
        val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep"))
        val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves"))
        val content =
            "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
        fillBlankView2.setFillContent(content, "")
    }

    private fun init3() {
        val image = FillBlankTagUtil.imageToHtml(R.mipmap.ic_launcher)
        val content =
            "A group of ____ are eating grass and ____ (leaf) in front of the farm.$image"
        fillBlankView3.setFillContent(content, "____")
    }

    private fun init4() {
        val answer1 = FillBlankTagUtil.blankToHtml(listOf("sheep"))
        val answer2 = FillBlankTagUtil.blankToHtml(listOf("leaves"))
        val content =
            "A group of $answer1(sheep) are eating grass and $answer2 (leaf) in front of the farm."
        fillBlankView4.setFillContent(content, "")
        fillBlankView4.showRightAnswer(true)
    }

}
