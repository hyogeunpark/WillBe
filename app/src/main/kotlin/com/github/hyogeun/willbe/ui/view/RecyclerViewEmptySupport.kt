package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by SAMSUNG on 2017-10-27.
 */
class RecyclerViewEmptySupport @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var emptyView: View? = null
    private var emptyObserver: AdapterDataObserver? = object : AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter?.itemCount == 0) {
                emptyView?.visibility = View.VISIBLE
                this@RecyclerViewEmptySupport.visibility = View.GONE
            } else {
                emptyView?.visibility = View.GONE
                this@RecyclerViewEmptySupport.visibility = View.VISIBLE
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            val adapter = adapter
            if (adapter?.itemCount == 0) {
                emptyView?.visibility = View.VISIBLE
                this@RecyclerViewEmptySupport.visibility = View.GONE
            } else {
                emptyView?.visibility = View.GONE
                this@RecyclerViewEmptySupport.visibility = View.VISIBLE
            }
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            val adapter = adapter
            if (adapter?.itemCount == 0) {
                emptyView?.visibility = View.VISIBLE
                this@RecyclerViewEmptySupport.visibility = View.GONE
            } else {
                emptyView?.visibility = View.GONE
                this@RecyclerViewEmptySupport.visibility = View.VISIBLE
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver?.onChanged()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

    fun getEmptyView(): View? {
        return emptyView
    }

    fun setEmptyObserver(observer: AdapterDataObserver) {
        emptyObserver?.let { adapter?.unregisterAdapterDataObserver(it) }
        emptyObserver = observer
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver?.onChanged()
    }
}