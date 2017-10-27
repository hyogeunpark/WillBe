package com.github.hyogeun.willbe.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ViewCalendarBinding
import com.github.hyogeun.willbe.ui.extensions.listen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


/**
 * Created by SAMSUNG on 2017-10-20.
 */
class CalendarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    // how many days to show, defaults to six weeks, 42 days
    private val DAYS_COUNT = 42

    // default date format
    private val DATE_FORMAT = "MMM yyyy"

    // date format
    private var dateFormat = loadDateFormat(attrs)

    // current displayed month
    private val currentDate = Calendar.getInstance()

    //event handling
    private var eventHandler: EventHandler? = null

    // seasons' rainbow
    private var rainbow = intArrayOf(R.color.summer, R.color.fall, R.color.winter, R.color.spring)

    // month-season association (northern hemisphere, sorry australia :)
    private var monthSeason = intArrayOf(2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2)

    private lateinit var mAdapter: CalendarAdapter
    private var binding: ViewCalendarBinding

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.view_calendar, this, false)
        addView(binding.root)
        assignClickHandlers()

        updateCalendar()
    }

    private fun loadDateFormat(attrs: AttributeSet?): String {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        // try to load provided date format, and fallback to default otherwise
        dateFormat = ta.getString(R.styleable.CalendarView_dateFormat) ?: DATE_FORMAT
        ta.recycle()
        return dateFormat
    }

    private fun assignClickHandlers() {
        // add one month and refresh UI

        binding.calendarNextButton.setOnClickListener({ _ ->
            currentDate.add(Calendar.MONTH, 1)
            updateCalendar()
        })

        // subtract one month and refresh UI
        binding.calendarPrevButton.setOnClickListener({ _ ->
            currentDate.add(Calendar.MONTH, -1)
            updateCalendar()
        })
    }

    /**
     * Display dates correctly in grid
     */
    fun updateCalendar() {
        updateCalendar(null)
    }

    /**
     * Display dates correctly in grid
     */
    fun updateCalendar(events: HashSet<Date>?) {
        val cells: ArrayList<Date> = ArrayList()
        val calendar: Calendar = currentDate.clone() as Calendar


        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val monthBeginningCell: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1


        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)


        // fill cells
        while (cells.size < DAYS_COUNT) {
            cells.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // update grid
        if (binding.calendarGrid.adapter == null) {
            mAdapter = CalendarAdapter(cells, events)
            binding.calendarGrid.adapter = mAdapter
        } else {
            mAdapter.refreshData(days = cells, eventDays = events)
        }

        // update title
        val sdf = SimpleDateFormat(dateFormat)
        binding.calendarDateDisplay.text = sdf.format(currentDate.time)

        // set header color according to current season
        val month: Int = currentDate.get(Calendar.MONTH)
        val season: Int = monthSeason[month]
        val color: Int = rainbow[season]

        binding.calendarHeader.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    @Suppress("DEPRECATION")
    inner class CalendarAdapter(days: ArrayList<Date>, eventDays: HashSet<Date>?) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
        // days with events
        private var eventDays: HashSet<Date>? = eventDays
        private var days: ArrayList<Date> = days

        fun refreshData(days: ArrayList<Date>, eventDays: HashSet<Date>?) {
            this.days.apply {
                clear()
                addAll(days)
            }
            this.eventDays = eventDays
            notifyDataSetChanged()
        }

        private fun getItem(position: Int): Date {
            return days[position]
        }

        override fun getItemCount(): Int {
            return days.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_calendar_day, parent, false)).listen { position, _ ->
                eventHandler?.onDayLongPress(getItem(position))
            }
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val today = Date()
            val date: Date = getItem(position)
            val day = date.date
            val month = date.month
            val year = date.year

            // clear styling
            (holder?.itemView as TextView).apply {
                setTypeface(null, Typeface.NORMAL)
                setTextColor(Color.BLACK)
                text = date.date.toString()
            }

            eventDays?.takeIf { it.contains(date) && !(year == today.year && month == today.month && day == today.date)}?.let {
                // if this day has an event, specify event image
                (holder.itemView as TextView).apply {
                    when (date.day) {
                        0 -> setTextColor(resources.getColor(android.R.color.holo_red_dark))
                        6 -> setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                        else -> setTextColor(resources.getColor(android.R.color.black))
                    }
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.event_circle, 0)
                }
            } ?: (holder.itemView as TextView).apply {
                if (month != currentDate.time.month || year != currentDate.time.year) {
                    // if this day is outside current month, grey it out
                    setTextColor(resources.getColor(R.color.greyed_out))
                } else if (year == today.year && month == today.month && day == today.date) {
                    // if it is currentDate, set it to blue/bold
                    setTypeface(null, Typeface.BOLD)
                    setTextColor(resources.getColor(android.R.color.white))
                    setBackgroundResource(R.drawable.today_circle)
                } else if (date.day == 0 || date.day == 6) {
                    when (date.day) {
                        0 -> setTextColor(resources.getColor(android.R.color.holo_red_dark))
                        6 -> setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                        else -> setTextColor(resources.getColor(android.R.color.black))
                    }
                }
            }
            (holder.itemView as TextView).width = display.width / 7
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }

    /**
     * Assign event handler to be passed needed events
     */
    fun setEventHandler(eventHandler: EventHandler) {
        this.eventHandler = eventHandler
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    interface EventHandler {
        fun onDayLongPress(date: Date)
    }
}