package com.github.hyogeun.willbe.ui.time

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.RowGridListTimeAlarmBinding
import com.github.hyogeun.willbe.databinding.RowLinearListTimeAlarmBinding
import com.github.hyogeun.willbe.model.Alarm
import com.github.hyogeun.willbe.ui.detail.AlarmDetailActivity

/**
 * Created by SAMSUNG on 2017-11-08.
 */
class AlarmAdapter(mode: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val GRID_MODE = 0
        val LINEAR_MODE = 1
    }

    private val mListData = ArrayList<Alarm>()
    private val mMode:Int = mode

    override fun getItemCount(): Int = 5

    override fun getItemViewType(position: Int): Int = mMode

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == GRID_MODE) {
            GridViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.row_grid_list_time_alarm, parent, false))
        } else {
            LinearViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.row_linear_list_time_alarm, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val mode = getItemViewType(position)
        if(mode == GRID_MODE) {
            (holder as GridViewHolder).setData()
        } else if(mode == LINEAR_MODE) {
            (holder as LinearViewHolder).setData()
        }
    }

    class GridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mBinding: RowGridListTimeAlarmBinding = DataBindingUtil.bind(itemView)

        fun setData() {
            Glide.with(itemView.context)
                    .load("http://blogfiles5.naver.net/20141007_267/nineps_1412644756693qW7MK_JPEG/yay8567378.jpg")
                    .into(mBinding.alarmListItemBackground)
        }
    }

    class LinearViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mBinding: RowLinearListTimeAlarmBinding = DataBindingUtil.bind(itemView)

        fun setData() {
            itemView.setOnClickListener { view ->
                AlarmDetailActivity.createInstance(view.context)
            }
            Glide.with(itemView.context)
                    .load("http://blogfiles8.naver.net/MjAxNzA2MDZfMjI2/MDAxNDk2NzU3ODgzMjgx.lwX-ZBDJcHHF67yG7eGhMM2BQqjHbIxKd76ThsloomAg.6ffi9JDp_ru0ay5cdzKSVTzEf9MX4FlP-3oL5y1qIE8g.PNG.daddy2015/%BC%BA%B0%F8%B8%ED%BE%F0%2C_%C0%CE%BB%FD%B8%ED%BE%F0%2C_%B8%F1%C7%A5%B8%A6_%C0%CC%B7%E7%B0%D4_%C7%CF%B4%C2_%C0%CE%BB%FD_%BC%BA%B0%F8_%B8%ED%BE%F0_%B8%F0%C0%BD_4.png")
                    .into(mBinding.alarmListItemBackground)
        }
    }
}