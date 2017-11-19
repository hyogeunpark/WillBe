package com.github.hyogeun.willbe.ui.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ActivityAlarmDetailBinding
import java.util.*

/**
 * Created by SAMSUNG on 2017-11-18.
 */
class AlarmDetailActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun createInstance(context: Context) {
            val intent = Intent(context, AlarmDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mBinding: ActivityAlarmDetailBinding
    private var isDayVisibility: ObservableBoolean = ObservableBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_detail)
        setSupportActionBar(mBinding.toolbar)
        with(supportActionBar!!) {
            title = "#명언 #맛스타그램 #워너비"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.alarmDetailViewPager.adapter = ImagePagerAdapter()
        with(mBinding.alarmDetailInfo!!) {
            alarmTimeMode.setOnCheckedChangeListener(this@AlarmDetailActivity)
            visibility = isDayVisibility
        }
    }

    override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
        isDayVisibility.set(isChecked)
        mBinding.alarmDetailInfo?.alarmTimeMode?.text = if (isChecked) "날짜 설정" else "요일 설정"
    }

    class ImagePagerAdapter : PagerAdapter() {

        private val mImageData = ArrayList<String>(Arrays.asList("http://blogfiles5.naver.net/20141007_267/nineps_1412644756693qW7MK_JPEG/yay8567378.jpg",
                "http://blogfiles8.naver.net/MjAxNzA2MDZfMjI2/MDAxNDk2NzU3ODgzMjgx.lwX-ZBDJcHHF67yG7eGhMM2BQqjHbIxKd76ThsloomAg.6ffi9JDp_ru0ay5cdzKSVTzEf9MX4FlP-3oL5y1qIE8g.PNG.daddy2015/%BC%BA%B0%F8%B8%ED%BE%F0%2C_%C0%CE%BB%FD%B8%ED%BE%F0%2C_%B8%F1%C7%A5%B8%A6_%C0%CC%B7%E7%B0%D4_%C7%CF%B4%C2_%C0%CE%BB%FD_%BC%BA%B0%F8_%B8%ED%BE%F0_%B8%F0%C0%BD_4.png"))

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val imageView = ImageView(container?.context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            Glide.with(container?.context)
                    .load(mImageData[position])
                    .into(imageView)
            container?.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean = (view == `object`)

        override fun getCount(): Int = 2

    }
}