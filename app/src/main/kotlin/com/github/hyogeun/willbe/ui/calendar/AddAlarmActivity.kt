package com.github.hyogeun.willbe.ui.calendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ActivityAddAlramBinding
import android.view.animation.Transformation
import android.view.animation.Animation
import android.widget.CompoundButton
import com.github.hyogeun.willbe.model.Alarm
import com.github.hyogeun.willbe.ui.common.BaseActivity
import io.realm.Realm
import java.util.*


/**
 * Created by SAMSUNG on 2017-10-28.
 */
/**
 * 알람 벨소리 관련
 *    https://www.androidpub.com/1701852
 *    http://www.masterqna.com/android/11659/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EA%B8%B0%EB%B3%B8-%EB%B2%A8%EC%86%8C%EB%A6%AC-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0-%EC%A7%88%EB%AC%B8-%EB%93%9C%EB%A6%BD%EB%8B%88%EB%8B%A4
 *    http://blog.naver.com/PostView.nhn?blogId=starwoin&logNo=90172557299
 */

//TODO 벨소리 선택
class AddAlarmActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    companion object {
        @JvmField val REQ_CODE_ADD_ALARM = AddAlarmActivity::class.hashCode().and(0x000000ff)
        fun createInstance(context: Activity) {
            val intent = Intent(context, AddAlarmActivity::class.java)
            context.startActivityForResult(intent, REQ_CODE_ADD_ALARM)
        }
    }

    private lateinit var mBinding: ActivityAddAlramBinding
    private var isDayVisibility: ObservableBoolean = ObservableBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_alram)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.addTag.setOnClickListener(this)
        with(mBinding.alarmInfo!!) {
            alarmDate.setOnClickListener(this@AddAlarmActivity)
            alarmDatePicker.init(alarmDatePicker.year, alarmDatePicker.month, alarmDatePicker.dayOfMonth
            ) { _, year, month, dayOfMonth ->
                alarmDate.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
            }
            alarmDatePicker.minDate = System.currentTimeMillis() - 1000
            alarmTimeMode.setOnCheckedChangeListener(this@AddAlarmActivity)
            visibility = isDayVisibility
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_alarm, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.save) {
            mBinding.alarmInfo?.let {
                val alarm = Alarm()
                //TODO 주석 해제 할것
//            alarm.tags.addAll(mBinding.tags.tags)
                alarm.tags.add("#워너비")
                alarm.tags.add("명언")
                val calendar: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    GregorianCalendar(it.alarmDatePicker.year, it.alarmDatePicker.month, it.alarmDatePicker.dayOfMonth,
                            it.alarmTimePicker.hour, it.alarmTimePicker.minute)
                } else {
                    GregorianCalendar(it.alarmDatePicker.year, it.alarmDatePicker.month, it.alarmDatePicker.dayOfMonth,
                            it.alarmTimePicker.currentHour, it.alarmTimePicker.currentMinute)
                }
                alarm.date = calendar.timeInMillis
                alarm.memo = it.alarmMemo.text.toString()
                alarm.instaImage.put(alarm.tags[0], "http://blogfiles5.naver.net/20141007_267/nineps_1412644756693qW7MK_JPEG/yay8567378.jpg")
                alarm.instaImage.put(alarm.tags[1], "http://blogfiles8.naver.net/MjAxNzA2MDZfMjI2/MDAxNDk2NzU3ODgzMjgx.lwX-ZBDJcHHF67yG7eGhMM2BQqjHbIxKd76ThsloomAg.6ffi9JDp_ru0ay5cdzKSVTzEf9MX4FlP-3oL5y1qIE8g.PNG.daddy2015/%BC%BA%B0%F8%B8%ED%BE%F0%2C_%C0%CE%BB%FD%B8%ED%BE%F0%2C_%B8%F1%C7%A5%B8%A6_%C0%CC%B7%E7%B0%D4_%C7%CF%B4%C2_%C0%CE%BB%FD_%BC%BA%B0%F8_%B8%ED%BE%F0_%B8%F0%C0%BD_4.png")
                val realm = Realm.getDefaultInstance()
                val index = realm.where(Alarm::class.java).max(Alarm.INDEX)
                alarm.index = index.toInt() + 1
                realm.executeTransaction {
                    realm.copyToRealmOrUpdate(alarm)
                    realm.close()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
        isDayVisibility.set(isChecked)
        mBinding.alarmInfo!!.alarmTimeMode.text = if (isChecked) "날짜 설정" else "요일 설정"
    }

    override fun onClick(view: View?) {
        if (view == mBinding.addTag) {
            mBinding.tags.addTagView(mBinding.autoCompleteTextView.text.toString())
            mBinding.autoCompleteTextView.setText("")
        } else if (view == mBinding.alarmInfo!!.alarmDate) {
            TransitionManager.beginDelayedTransition(mBinding.scrollview, ChangeBounds())
            mBinding.alarmInfo?.let {
                it.alarmDatePicker.visibility = if (it.alarmDatePicker.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                val buttonImage = if (it.alarmDatePicker.visibility == View.VISIBLE) R.mipmap.ic_expand_less_black else R.mipmap.ic_expand_more_black
                it.alarmDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, buttonImage, 0)
            }
        }
    }
}