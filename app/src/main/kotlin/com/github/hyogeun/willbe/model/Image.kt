package com.github.hyogeun.willbe.model

import com.github.hyogeun.willbe.data.RealmString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by SAMSUNG on 2017-11-29.
 */
class Image(key: String = "", value:RealmList<RealmString> = RealmList()): RealmObject() {
    @PrimaryKey
    var tag:String = key
    var images:RealmList<RealmString> = value

}