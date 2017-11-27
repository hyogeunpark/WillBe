package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.ui.extensions.convertDpToPx
import com.google.android.flexbox.*

/**
 * Created by SAMSUNG on 2017-10-28.
 */
class TagLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr), View.OnClickListener {

    val tags:ArrayList<String> = ArrayList()

    init {
        flexWrap = FlexWrap.WRAP
    }

    fun addTagView(tag: String) {
        tags.add(tag)
        val tagView = TextView(context).apply {
            text = "#" + tag
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)
            gravity = Gravity.CENTER
            setPadding(context.convertDpToPx(15).toInt(),
                    context.convertDpToPx(5).toInt(),
                    context.convertDpToPx(10).toInt(),
                    context.convertDpToPx(5).toInt())
            setSingleLine(true)
            setBackgroundResource(R.drawable.shape_tag)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_clear_white, 0)
            compoundDrawablePadding = context.convertDpToPx(5).toInt()
            setOnClickListener(this@TagLayout)
            setTag(tag)
        }
        val params:FlexboxLayout.LayoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, context.convertDpToPx(10).toInt(), context.convertDpToPx(10).toInt())
        addView(tagView, params)
    }

    override fun onClick(view: View?) {
        tags.remove(view?.tag.toString())
        removeView(view)
    }

    fun clearTag() {
        tags.clear()
        removeAllViews()
    }

}