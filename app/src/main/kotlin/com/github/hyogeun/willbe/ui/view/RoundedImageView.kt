package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import com.github.hyogeun.willbe.R


/**
 * Created by SAMSUNG on 2017-11-08.
 */
class RoundedImageView : ImageView {

    private val CORNER_NONE = 0
    private val CORNER_TOP_LEFT = 1
    private val CORNER_TOP_RIGHT = 2
    private val CORNER_BOTTOM_RIGHT = 4
    private val CORNER_BOTTOM_LEFT = 8
    private val CORNER_ALL = 15
    private val SQUARE_NONE = 0
    private val SQUARE_WIDTH = 1
    private val SQUARE_HEIGHT = 2

    private val cornerRect = RectF()
    private val path = Path()
    private var cornerRadius:Int = 0
    private var roundedCorners:Int = 0
    private var squareMode = SQUARE_NONE

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        attrs?.run {
            val a:TypedArray = context.obtainStyledAttributes(this, R.styleable.RoundedImageView)
            cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, 0)
            roundedCorners = a.getInt(R.styleable.RoundedImageView_roundedCorners, CORNER_NONE)
            squareMode = a.getInt(R.styleable.RoundedImageView_squareMode, SQUARE_NONE)
            a.recycle()
            invalidate()
        }
    }

    fun setCornerRadius(radius:Int) {
        cornerRadius = radius
        setPath()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when(squareMode) {
            SQUARE_WIDTH -> super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            SQUARE_HEIGHT -> super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            else -> super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        when (squareMode) {
            SQUARE_WIDTH -> super.onSizeChanged(w, w, oldw, oldh)
            SQUARE_HEIGHT -> super.onSizeChanged(h, h, oldw, oldh)
            else -> super.onSizeChanged(w, h, oldw, oldh)
        }
        setPath()
    }

    override fun onDraw(canvas: Canvas?) {
        if(!path.isEmpty) {
            canvas?.clipPath(path)
        }
        super.onDraw(canvas)
    }

    private fun setPath() {
        path.rewind()

        if(cornerRadius >= 1f && roundedCorners != CORNER_NONE) {
            val twoRadius = cornerRadius * 2
            cornerRect.set(-cornerRadius.toFloat(), -cornerRadius.toFloat(), cornerRadius.toFloat(), cornerRadius.toFloat())

            if(isRounded(CORNER_TOP_LEFT)) {
                cornerRect.offsetTo(0f, 0f)
                path.arcTo(cornerRect, 180f, 90f)
            } else {
                path.moveTo(0f, 0f)
            }

            if(isRounded(CORNER_TOP_RIGHT)) {
                cornerRect.offsetTo((width - twoRadius).toFloat(), 0f)
                path.arcTo(cornerRect, 270f, 90f)
            } else {
                path.lineTo(width.toFloat(), 0f)
            }

            if(isRounded(CORNER_BOTTOM_RIGHT)) {
                cornerRect.offsetTo((width - twoRadius).toFloat(), (height - twoRadius).toFloat())
                path.arcTo(cornerRect, 0f, 90f)
            } else {
                path.lineTo(width.toFloat(), height.toFloat())
            }

            if(isRounded(CORNER_BOTTOM_LEFT)) {
                cornerRect.offsetTo(0f, (height - twoRadius).toFloat())
                path.arcTo(cornerRect, 90f, 90f)
            } else {
                path.lineTo(0f, height.toFloat())
            }

            path.close()
        }
    }

    private fun isRounded(corner:Int) : Boolean  = corner.and(roundedCorners) == corner
}