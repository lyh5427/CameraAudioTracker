package com.yunho.king

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication: Application() {


    override fun onCreate() {
        super.onCreate()
    }

    companion object {

        @Volatile
        var prefs: Prefs? = null

        val TagName = "King"
    }
}