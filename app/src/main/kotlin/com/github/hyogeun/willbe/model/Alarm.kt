package com.github.hyogeun.willbe.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

/**
 * Created by SAMSUNG on 2017-11-01.
 */
class Alarm: RealmObject() {
    companion object {
        @JvmField val INDEX = "index"
    }

    @PrimaryKey
    @Index
    var index:Int = 0
    var date:Long = -1
    var memo:String = ""
    val tags:ArrayList<String> = ArrayList()

    //TODO: RealmObject not support HashMap
    var instaImage:HashMap<String, String> = HashMap()
}