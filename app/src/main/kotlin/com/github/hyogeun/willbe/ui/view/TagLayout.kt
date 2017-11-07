package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.ui.extensions.convertDpToPx

/**
 * Created by SAMSUNG on 2017-10-28.
 */
class TagLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    val tags:ArrayList<String> = ArrayList()

    fun addTagView(tag: String) {
        tags.add(tag)
        val tagView = TextView(context).apply {
            text = "#" + tag
            textSize = context.convertDpToPx(5)
            gravity = Gravity.CENTER
            setPadding(context.convertDpToPx(1).toInt(),
                    context.convertDpToPx(1).toInt(),
                    context.convertDpToPx(1).toInt(),
                    context.convertDpToPx(1).toInt())
            setBackgroundResource(R.drawable.shape_tag)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_clear_white, 0)
            setOnClickListener(this@TagLayout)
            setTag(tag)
        }
        val params:LinearLayout.LayoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, context.convertDpToPx(25).toInt())
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