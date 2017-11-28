package com.github.hyogeun.willbe.data

import com.github.hyogeun.willbe.BuildConfig
import io.realm.RealmMigration
import io.realm.RealmList
import android.graphics.drawable.Drawable
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import io.realm.RealmConfiguration
import io.realm.RealmObject
import java.util.*
import java.lang.reflect.Type


/**
 * Created by SAMSUNG on 2017-11-28.
 */
class RealmUtils {
    companion object {
        private val VERSION: Int = BuildConfig.VERSION_CODE
        private val MIGRATION: RealmMigration = RealmMigration { realm, oldVersion, newVersion ->
            val schema = realm.schema
            if (oldVersion.toInt() == 0) {
                schema.create("Insta")
                schema.create("RealmString").addField("value", String::class.java)
                schema.create("Image")
                        .addField("tag", String::class.java)
                        .addRealmListField("images", schema.get("RealmString"))

                schema.create("Alarm")
                        .addField("index", Int::class.java)
                        .addField("date", Long::class.java)
                        .addField("memo", String::class.java)
                        .addRealmListField("instaImage", schema.get("Image"))

                oldVersion.plus(1)
            }
        }
        @JvmField val CONFIG_ALARM:RealmConfiguration = RealmConfiguration.Builder().name("willbe.realm.alarm").schemaVersion(VERSION.toLong()).migration(MIGRATION).build()
        @JvmField val REALM_GSON = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean =
                    f.declaringClass == RealmObject::class.java || f.declaringClass == Drawable::class.java

            override fun shouldSkipClass(clazz: Class<*>): Boolean = false
        }).registerTypeAdapter(object : TypeToken<RealmList<RealmString>>() {

        }.type, RealmStringDeserializer()).registerTypeAdapter(Date::class.java, DateDeserializer()).registerTypeAdapter(Date::class.java, DateSerializer()).create()

        class RealmStringDeserializer : JsonDeserializer<RealmList<RealmString>> {
            @Throws(JsonParseException::class)
            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RealmList<RealmString> {
                val realmStrings = RealmList<RealmString>()
                val stringList = json.asJsonArray
                for (stringElement in stringList) {
                    realmStrings.add(RealmString(stringElement.asString))
                }
                return realmStrings
            }
        }
    }
    class DateSerializer : JsonSerializer<Date> {
        override fun serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
                JsonPrimitive(src.time / 1000)
    }

    class DateDeserializer : JsonDeserializer<Date> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date =
                Date(json.asLong * 1000)
    }
}