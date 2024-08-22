package com.yunho.king

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@HiltAndroidApp
class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
            prefs = Prefs(this)
//        val dexOutputDir: File = codeCacheDir
//        dexOutputDir.setReadOnly()
    }

    private fun setAdmob() {
        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@GlobalApplication) {}
        }
    }

    companion object {

        @Volatile
        var prefs: Prefs? = null

        val TagName = "King"
    }
}