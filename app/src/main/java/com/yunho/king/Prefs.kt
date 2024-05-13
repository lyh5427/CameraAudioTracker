package com.yunho.king

import android.content.Context
import android.content.SharedPreferences

class Prefs (context : Context) {
    val PREFS_FILENAME = "com.yunho.kinh.prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME,
        Context.MODE_PRIVATE
    )

    var firstOpenApp: Boolean
        get() = prefs.getBoolean("firstPermissionCheck", true)
        set(value) = prefs.edit().putBoolean("firstPermissionCheck", value).apply()
}