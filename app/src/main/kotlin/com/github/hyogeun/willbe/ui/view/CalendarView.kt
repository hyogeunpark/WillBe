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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ViewCalendarBinding
import com.github.hyogeun.willbe.ui.extensions.listen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import android.view.WindowManager
import android.view.Display





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

    private var binding: ViewCalendarBinding

    init {
        val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.view_calendar, this, false)
        addView(binding.root)
        assignClickHandlers()

        updateCalendar()
    }

    private fun loadDateFormat(attrs: AttributeSet?): String  {
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
    fun updateCalendar(events: HashSet<Date>?)
    {
        val cells: ArrayList<Date> = ArrayList()
        val calendar: Calendar = currentDate.clone() as Calendar


        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val monthBeginningCell: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1


        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)


        // fill cells
        while (cells.size < DAYS_COUNT)
        {
            cells.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // update grid
        binding.calendarGrid.adapter = CalendarAdapter(cells, events)

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
    inner class CalendarAdapter(days: ArrayList<Date>, eventDays: HashSet<Date>?): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
        // days with events
        private var eventDays: HashSet<Date>? = eventDays
        private var days:ArrayList<Date> = days
        init {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
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
            val date:Date = getItem(position)
            val day = date.day
            val month = date.month
            val year = date.year

            // today
            val today = Date()

            // if this day has an event, specify event image
            (holder?.itemView as TextView).setBackgroundResource(0)
            eventDays?.let {
                for (eventDate in it) {
                    if (eventDate.date == day && eventDate.month == month && eventDate.year == year) {
                        // mark this day for event
                        holder.itemView.setBackgroundResource(R.drawable.event_circle)
                        break
                    }
                }
            }

            // clear styling
            (holder.itemView as TextView).apply {
                setTypeface(null, Typeface.NORMAL)
                setTextColor(Color.BLACK)
                text = date.date.toString()
            }

            if (month != today.month || year != today.year) {
                // if this day is outside current month, grey it out
                holder.itemView.setTextColor(resources.getColor(R.color.greyed_out))
            }
            else if (day == today.date) {
                // if it is today, set it to blue/bold
                holder.itemView.let {
                    it.setTypeface(null, Typeface.BOLD)
                    it.setTextColor(resources.getColor(R.color.today))
                }
            }
            holder.itemView.width = display.width/7
        }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    }

    /*@Suppress("DEPRECATION")
    inner class CalendarAdapter(context: Context?, days: ArrayList<Date>?, eventDays: HashSet<Date>?) : ArrayAdapter<Date>(context, R.layout.view_calendar_day, days)
    {
        // days with events
        private var eventDays: HashSet<Date>? = eventDays

        // for view inflation
        private var inflater:LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // day in question
            val date:Date = getItem(position)
            val day = date.day
            val month = date.month
            val year = date.year

            // inflate item if it does not exist yet
            var retView: View = convertView ?: inflater.inflate(R.layout.view_calendar_day, parent, false)

            // today
            val today = Date()

            // if this day has an event, specify event image
            retView.setBackgroundResource(0)
            eventDays?.let {
                for (eventDate in it) {
                    if (eventDate.date == day && eventDate.month == month && eventDate.year == year) {
                        // mark this day for event
                        retView.setBackgroundResource(R.drawable.event_circle)
                        break
                    }
                }
            }

            // clear styling
            (retView as TextView).apply {
                setTypeface(null, Typeface.NORMAL)
                setTextColor(Color.BLACK)
                text = date.date.toString()
            }

            if (month != today.month || year != today.year)
            {
                // if this day is outside current month, grey it out
                retView.setTextColor(resources.getColor(R.color.greyed_out))
            }
            else if (day == today.date)
            {
                // if it is today, set it to blue/bold
                retView.let {
                    it.setTypeface(null, Typeface.BOLD)
                    it.setTextColor(resources.getColor(R.color.today))
                }
            }

            return retView
        }
    }*/

    /**
     * Assign event handler to be passed needed events
     */
    fun setEventHandler(eventHandler: EventHandler)
    {
        this.eventHandler = eventHandler
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    interface EventHandler
    {
        fun onDayLongPress(date: Date)
    }
}