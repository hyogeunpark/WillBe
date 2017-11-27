package com.github.hyogeun.willbe.ui.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ActivityAlarmBinding
import com.github.hyogeun.willbe.ui.common.BaseActivity

/**
 * Created by SAMSUNG on 2017-11-23.
 */
class AlarmActivity: BaseActivity() {

    companion object {
        fun createInstance(context: Context) {
            val intent = Intent(context, AlarmActivity::class.java)
            context.startActivity(intent)
        }
    }
    private lateinit var mBinding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        Glide.with(this)
                .load("http://blogfiles5.naver.net/20141007_267/nineps_1412644756693qW7MK_JPEG/yay8567378.jpg")
                .into(mBinding.alarmTagImage)
        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_shake)
        anim.repeatCount = Animation.INFINITE
        mBinding.alarmTimeImg.startAnimation(anim)
    }
}