package com.yunho.king

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setAdmob()
    }

    private fun setAdmob() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@GlobalApplication) {}
        }
    }

    companion object {
        val TagName = "King"
    }
}
