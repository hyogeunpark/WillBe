package com.github.hyogeun.willbe.ui.calendar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.FragmentCalendarBinding
import com.github.hyogeun.willbe.ui.time.AlarmAdapter
import com.github.hyogeun.willbe.ui.view.CalendarView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by SAMSUNG on 2017-10-20.
 */
//http://thdev.tech/androiddev/2016/12/11/Android-BottomSheet-Intro.html -> Bottom Sheet
class CalendarFragment: Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    private lateinit var mBinding : FragmentCalendarBinding
    private val mEventsDay: HashSet<Date> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mBinding.calendarContentRecyclerView) {
            setEmptyView(mBinding.emptyView)
            adapter = AlarmAdapter(AlarmAdapter.LINEAR_MODE)
        }
        mBinding.emptyView.visibility = View.GONE
        val bottomSheetBehavior = BottomSheetBehavior.from(mBinding.listBottomSheet)
        bottomSheetBehavior.state = STATE_COLLAPSED
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
        initCalendar()
    }

    private fun initCalendar() {
        mBinding.calendarView.setEventHandler(object: CalendarView.EventHandler {
            override fun onDayLongPress(date: Date) {
                val df: DateFormat = SimpleDateFormat.getDateInstance()
                Snackbar.make(mBinding.root, df.format(date), Snackbar.LENGTH_SHORT).show()
                mEventsDay.takeUnless { it.contains(date) }?.add(date)
                mBinding.calendarView.updateCalendar(mEventsDay)
            }
        })
    }
}