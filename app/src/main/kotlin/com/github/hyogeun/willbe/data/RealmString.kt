package com.github.hyogeun.willbe.data

import io.realm.RealmObject

/**
 * Created by SAMSUNG on 2017-11-29.
 */
class RealmString(str:String = ""): RealmObject() {

    var value:String = str

    override fun toString(): String = value
}