package com.github.hyogeun.willbe.ui.extensions

import android.support.v7.widget.RecyclerView

/**
 * Created by SAMSUNG on 2017-10-21.
 */
fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}