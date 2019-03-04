package com.cnting.fillblankview

import org.xml.sax.XMLReader

/**
 * Created by cnting on 2019-03-01
 * 自定义标签转化
 */
object FillBlankTagUtil {

    const val TAG_BLANK = "fillblank"
    const val ATTR_RIGHT_ANSWER = "rightanswer"
    const val ATTR_USER_ANSWER = "useranswer"

    /**
     * 拼接成<fillblank rightanswer='answer1;answer2;answer3' userAnswer='userAnswer'>
     * 一个空可能有多个正确答案
     */
    fun blankToHtml(rightAnswers: List<String>/*, userAnswer: String*/): String {
        val answers = rightAnswers.joinToString(";")
//        return "<$TAG_BLANK $ATTR_RIGHT_ANSWER='$answers'> $ATTR_USER_ANSWER='$userAnswer'"
        return "&nbsp;<$TAG_BLANK $ATTR_RIGHT_ANSWER='$answers'>&nbsp;"
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