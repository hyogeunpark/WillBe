package com.github.hyogeun.willbe

import android.app.Application
import io.realm.Realm

/**
 * Created by SAMSUNG on 2017-11-28.
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}