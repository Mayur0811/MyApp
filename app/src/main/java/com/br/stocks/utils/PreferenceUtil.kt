package com.br.stocks.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import info.metadude.android.typedpreferences.*
import java.util.*

object PreferenceUtil {

    private var preferences: SharedPreferences? = null
    private var preferenceMap: MutableMap<String, Any> = HashMap()

    fun clearPreference() {
        val editor = preferences!!.edit()
        editor.clear()
        editor.apply()
        preferenceMap.clear()
    }

    fun booleanPreference(key: String, defaultValue: Boolean): BooleanPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                BooleanPreference(preferences, key, defaultValue)
        }
        return preferenceMap[key] as BooleanPreference?
    }

    fun booleanPreference(key: String): BooleanPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                BooleanPreference(preferences, key)
        }
        return preferenceMap[key] as BooleanPreference?
    }

    fun intPreference(key: String, defaultValue: Int): IntPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                IntPreference(preferences, key, defaultValue)
        }
        return preferenceMap[key] as IntPreference?
    }

    fun intPreference(key: String): IntPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                IntPreference(preferences, key)
        }
        return preferenceMap[key] as IntPreference?
    }

    fun longPreference(key: String, defaultValue: Int): LongPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                LongPreference(
                    preferences, key,
                    defaultValue.toLong()
                )
        }
        return preferenceMap[key] as LongPreference?
    }

    fun longPreference(key: String): LongPreference? {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                LongPreference(preferences, key)
        }
        return preferenceMap[key] as LongPreference?
    }

    fun stringPreference(key: String): StringPreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                StringPreference(preferences, key)
        }
        return preferenceMap[key] as StringPreference
    }

    fun stringPreference(key: String, defaultValue: String?): StringPreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                StringPreference(preferences, key, defaultValue)
        }
        return preferenceMap[key] as StringPreference
    }

//    fun user(): ModelPreference<User> {
//        return ModelPreference(User::class.java)
//    }

    fun doublePreference(key: String, defaultValue: Int): DoublePreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                DoublePreference(
                    preferences, key,
                    defaultValue.toDouble()
                )
        }
        return preferenceMap[key] as DoublePreference
    }

    fun doublePreference(key: String): DoublePreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                IntPreference(preferences, key)
        }
        return preferenceMap[key] as DoublePreference
    }

    fun floatPreference(key: String, defaultValue: Int): FloatPreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                FloatPreference(
                    preferences, key,
                    defaultValue.toFloat()
                )
        }
        return preferenceMap[key] as FloatPreference
    }

    fun floatPreference(key: String): FloatPreference {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap[key] =
                FloatPreference(preferences, key)
        }
        return preferenceMap[key] as FloatPreference
    }

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(CommonsCore.context)
    }
}