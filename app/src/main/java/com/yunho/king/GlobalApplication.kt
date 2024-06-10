package com.yunho.king

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
            prefs = Prefs(this)
//        val dexOutputDir: File = codeCacheDir
//        dexOutputDir.setReadOnly()
    }

    companion object {

        @Volatile
        var prefs: Prefs? = null

        val TagName = "King"
    }
}