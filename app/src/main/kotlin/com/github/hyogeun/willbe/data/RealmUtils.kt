package com.github.hyogeun.willbe.data

import com.github.hyogeun.willbe.BuildConfig
import io.realm.RealmMigration

/**
 * Created by SAMSUNG on 2017-11-28.
 */
class RealmUtils {
    private val VERSION:Int  = BuildConfig.VERSION_CODE
    private val MIGRATION:RealmMigration = RealmMigration { realm, oldVersion, newVersion ->
        val schema = realm.schema
        if(oldVersion.toInt() == 1) {
            schema.create("Insta")
            schema.create("Alarm").addField("index", Int::class.java).addField("")

            oldVersion.plus(1)
        }
    }
}