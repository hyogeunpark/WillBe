package com.github.hyogeun.willbe.ui.time

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.data.RealmUtils
import com.github.hyogeun.willbe.databinding.FragmentTimeBinding
import com.github.hyogeun.willbe.model.Alarm
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by SAMSUNG on 2017-10-20.
 */
class TimeFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): TimeFragment = TimeFragment()
    }

    private lateinit var mBinding: FragmentTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_time, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.calendarContentRecyclerView.setEmptyView(mBinding.emptyView)
        mBinding.calendarContentRecyclerView.adapter = AlarmAdapter(AlarmAdapter.GRID_MODE)
        val realm = Realm.getInstance(RealmUtils.CONFIG_ALARM)
        val alarms = realm.where(Alarm::class.java).apply {
            findAll().addChangeListener { results: RealmResults<Alarm> ->
                Log.i("PHG alarm size", results.size.toString())
            }
        }
        realm.close()
    }
}