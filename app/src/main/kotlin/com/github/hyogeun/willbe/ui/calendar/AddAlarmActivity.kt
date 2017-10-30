package com.github.hyogeun.willbe.ui.calendar

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ActivityAddAlramBinding
import android.view.animation.Transformation
import android.view.animation.Animation


/**
 * Created by SAMSUNG on 2017-10-28.
 */
class AddAlarmActivity: AppCompatActivity(), View.OnClickListener {

    companion object {
        fun createInstance(context: Context) {
            val intent = Intent(context, AddAlarmActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mBinding: ActivityAddAlramBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_alram)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.addTag.setOnClickListener(this)
        mBinding.alarmDate.setOnClickListener(this)
        mBinding.alarmDatePicker.init(mBinding.alarmDatePicker.year, mBinding.alarmDatePicker.month, mBinding.alarmDatePicker.dayOfMonth
        ) { _, year, month, dayOfMonth ->
            mBinding.alarmDate.text = String.format("%d년 %d월 %d일", year, month+1, dayOfMonth)
        }
        mBinding.alarmDatePicker.minDate = System.currentTimeMillis() - 1000
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_alarm, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.save) {
            //TODO 알람 저장
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if(view == mBinding.addTag) {
            mBinding.tags.addTagView(mBinding.autoCompleteTextView.text.toString())
            mBinding.autoCompleteTextView.setText("")
        } else if(view == mBinding.alarmDate) {
            if(mBinding.alarmDatePicker.visibility == View.GONE) {
                expand(mBinding.alarmDatePicker)
                mBinding.alarmDate.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.ic_expand_less_black, 0)
            } else {
                collapse(mBinding.alarmDatePicker)
                mBinding.alarmDate.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.ic_expand_more_black, 0)
            }
        }
    }

    private fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
        v.postDelayed({ v.visibility = View.VISIBLE }, 150)
    }

    private fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
    }
}