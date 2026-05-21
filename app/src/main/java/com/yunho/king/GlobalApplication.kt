package com.yunho.king

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.yunho.king.presentation.receiver.InstallReceiver
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class GlobalApplication: Application() {
    lateinit var installReceiver: InstallReceiver
    override fun onCreate() {
        super.onCreate()
        regReceiver()

//        val dexOutputDir: File = codeCacheDir
//        dexOutputDir.setReadOnly()
    }

    private fun setAdmob() {
        CoroutineScope(Dispatchers.IO).launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@GlobalApplication) {}
        }
    }

    private fun regReceiver() {
        installReceiver = InstallReceiver()

        val installFilter =
            IntentFilter()
                .apply {
                    addAction(Intent.ACTION_PACKAGE_ADDED)
                    addAction(Intent.ACTION_PACKAGE_REMOVED)
                    addDataScheme("package")
                }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.registerReceiver(installReceiver, installFilter, RECEIVER_EXPORTED)
        } else  {
            applicationContext.registerReceiver(installReceiver, installFilter)
        }
    }

    companion object {
        val TagName = "King"
    }
}