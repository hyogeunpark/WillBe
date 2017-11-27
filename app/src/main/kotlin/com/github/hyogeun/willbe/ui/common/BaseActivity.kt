package com.github.hyogeun.willbe.ui.common

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * Created by SAMSUNG on 2017-11-21.
 */
open class BaseActivity: AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}