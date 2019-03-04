package com.cnting.fillblankview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import androidx.appcompat.content.res.AppCompatResources

/**
 * Created by cnting on 2019-03-04
 * https://blog.csdn.net/u010418593/article/details/9324101
 */
internal class FillBlankImageGetter(private val context: Context) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable? {
        val id = source?.toInt()
        if (id != null) {
            val drawable = AppCompatResources.getDrawable(context, id)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
        return null
    }

}