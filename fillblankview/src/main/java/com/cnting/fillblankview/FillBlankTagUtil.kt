package com.cnting.fillblankview

import org.xml.sax.XMLReader
import java.lang.StringBuilder

/**
 * Created by cnting on 2019-03-01
 * 自定义标签转化
 */
object FillBlankTagUtil {

    const val TAG_BLANK = "fillblank"
    const val ATTR_RIGHT_ANSWER = "rightanswer"
    const val ATTR_USER_ANSWER = "useranswer"
    const val ATTR_IS_RIGHT = "isright"
    const val RIGHT_ANSWER_SPLIT = "/"

    /**
     * 拼接成<fillblank rightanswer='answer1/answer2/answer3' userAnswer='userAnswer'>
     * @param rightAnswers 一个空可能有多个正确答案
     * @param userAnswer 用户输入的答案
     * @param isRight 是否正确，如果不输入，则判断内容是否相等
     */
    fun blankToHtml(rightAnswers: List<String>, userAnswer: String = "", isRight: Boolean? = null): String {
        val answers = rightAnswers.joinToString(RIGHT_ANSWER_SPLIT)
        val stringBuilder = StringBuilder()
        stringBuilder.append("&nbsp;")
        stringBuilder.append("<")
        stringBuilder.append(TAG_BLANK)
        stringBuilder.append(" $ATTR_RIGHT_ANSWER='$answers' ")
        stringBuilder.append(" $ATTR_USER_ANSWER='$userAnswer' ")
        if (isRight != null) {
            stringBuilder.append(" $ATTR_IS_RIGHT='$isRight' ")
        }
        stringBuilder.append(">")
        stringBuilder.append("&nbsp;")
        return stringBuilder.toString()
    }

    /**
     * 分隔符变成下划线
     */
    fun splitToBlank(content: String, split: String): String {
        return content.replace(split, "&nbsp;<$TAG_BLANK>&nbsp;")
    }

    fun imageToHtml(resId: Int): String {
        return "<img src='$resId'>"
    }

    /**
     * 获取标签内属性
     */
    fun processAttribute(xmlReader: XMLReader): Map<String, String> {
        val elementField = xmlReader.javaClass.getDeclaredField("theNewElement")
        elementField.isAccessible = true
        val element = elementField.get(xmlReader)
        val attsField = element.javaClass.getDeclaredField("theAtts")
        attsField.isAccessible = true
        val atts = attsField.get(element)
        val dataField = atts.javaClass.getDeclaredField("data")
        dataField.isAccessible = true
        val dataObj = dataField.get(atts) ?: return emptyMap()
        val data = dataObj as Array<String>
        val lengthField = atts.javaClass.getDeclaredField("length")
        lengthField.isAccessible = true
        val len = lengthField.get(atts) as Int

        val map = mutableMapOf<String, String>()
        (0 until len).forEach {
            map[data[it * 5 + 1]] = data[it * 5 + 4]
        }
        return map
    }
}