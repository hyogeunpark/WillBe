package com.github.hyogeun.willbe.model

import com.github.hyogeun.willbe.data.RealmString
import io.realm.RealmList
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
    val tags:RealmList<RealmString> = RealmList()
    var instaImage:RealmList<Image> = RealmList()
}