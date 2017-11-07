package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.ImageView
import com.github.hyogeun.willbe.ui.extensions.convertDpToPx


/**
 * Created by SAMSUNG on 2017-11-08.
 */
class RoundedImageView : ImageView {
    private var mMaskPath: Path? = null
    private val mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCornerRadius:Int = 10

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null)
        mMaskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mMaskPaint.color = context.resources.getColor(android.R.color.transparent)

        mCornerRadius = context.convertDpToPx(12).toInt()
    }

    /**
     * Set the corner radius to use for the RoundedRectangle.
     */
    fun setCornerRadius(cornerRadius: Int) {
        mCornerRadius = cornerRadius
        generateMaskPath(width, height)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        if (w != oldW || h != oldH) {
            generateMaskPath(w, h)
        }
    }

    private fun generateMaskPath(w: Int, h: Int) {
        mMaskPath = Path()
        mMaskPath!!.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), mCornerRadius.toFloat(), mCornerRadius.toFloat(), Path.Direction.CW)
        mMaskPath!!.fillType = Path.FillType.INVERSE_WINDING
    }

    override fun onDraw(canvas: Canvas) {
        if (canvas.isOpaque) { // If canvas is opaque, make it transparent
            canvas.saveLayerAlpha(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), 255, Canvas.ALL_SAVE_FLAG)
        }

        super.onDraw(canvas)

        if (mMaskPath != null) {
            canvas.drawPath(mMaskPath, mMaskPaint)
        }
    }
}