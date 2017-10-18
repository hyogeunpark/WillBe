package com.github.hyogeun.willbe.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.hyogeun.willbe.R

/**
 * Created by SAMSUNG on 2017-10-18.
 */

// 잠금화면 위에 액티비티 띄우기 :  http://hns17.tistory.com/entry/App-%EA%B0%9C%EB%B0%9C-%EC%9E%A0%EA%B8%88%ED%99%94%EB%A9%B4-LockScreen-%EC%9C%84%EC%97%90-Activity-%EB%9D%84%EC%9A%B0%EA%B8%B0
class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}