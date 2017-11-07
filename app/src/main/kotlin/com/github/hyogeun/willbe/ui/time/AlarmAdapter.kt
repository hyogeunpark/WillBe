package com.github.hyogeun.willbe.ui.time

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.GridListTimeAlarmBinding
import com.github.hyogeun.willbe.model.Alarm
import com.github.hyogeun.willbe.ui.extensions.convertDpToPx
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by SAMSUNG on 2017-11-08.
 */
class AlarmAdapter: RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    private val mListData = ArrayList<Alarm>()

    override fun getItemCount(): Int {
        return 5
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.grid_list_time_alarm, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setData()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mBinding: GridListTimeAlarmBinding = DataBindingUtil.bind(itemView)

        fun setData() {
            Glide.with(itemView.context)
                    .load("https://i1.daumcdn.net/thumb/C720x360/?fname=http://t1.daumcdn.net/brunch/service/user/msj/image/0GNp_iY09IIyLNP2hBFSoK50hcQ.png")
                    .into(mBinding.alarmListItemBackground)
        }
    }
}