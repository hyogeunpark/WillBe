package com.github.hyogeun.willbe.ui.main

import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.github.hyogeun.willbe.R
import com.github.hyogeun.willbe.databinding.ActivityMainBinding
import com.github.hyogeun.willbe.ui.calendar.CalendarFragment
import com.github.hyogeun.willbe.ui.time.TimeFragment

/**
 * Created by SAMSUNG on 2017-10-18.
 */

// 잠금화면 위에 액티비티 띄우기 :  http://hns17.tistory.com/entry/App-%EA%B0%9C%EB%B0%9C-%EC%9E%A0%EA%B8%88%ED%99%94%EB%A9%B4-LockScreen-%EC%9C%84%EC%97%90-Activity-%EB%9D%84%EC%9A%B0%EA%B8%B0
class MainActivity: AppCompatActivity() {
    private lateinit var mBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(mBinding.toolbar)
        mBinding.mainViewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        mBinding.mainTabLayout.setupWithViewPager(mBinding.mainViewPager)
        /** set tab icon **/
        mBinding.mainTabLayout.getTabAt(0)?.icon = ContextCompat.getDrawable(this@MainActivity, R.mipmap.timer).apply {
            setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        }
        mBinding.mainTabLayout.getTabAt(1)?.icon = ContextCompat.getDrawable(this@MainActivity, R.mipmap.calendar_clock)
        mBinding.mainTabLayout.addOnTabSelectedListener(object: TabLayout.ViewPagerOnTabSelectedListener(mBinding.mainViewPager) {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                super.onTabUnselected(tab)
                tab?.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, android.R.color.white), PorterDuff.Mode.SRC_IN)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                super.onTabSelected(tab)
                tab?.icon?.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    inner class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        private val tabTitles = arrayOf(getString(R.string.tab_time), getString(R.string.tab_calendar))
        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> TimeFragment.newInstance()
                1 -> CalendarFragment.newInstance()
                else -> Fragment()
            }
        }
    }
}