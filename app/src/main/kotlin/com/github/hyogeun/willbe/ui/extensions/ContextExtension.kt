package com.github.hyogeun.willbe.ui.extensions

import android.content.Context
import android.util.TypedValue

/**
 * Created by SAMSUNG on 2017-10-28.
 */
fun Context.convertDpToPx(dp: Int) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics)
}