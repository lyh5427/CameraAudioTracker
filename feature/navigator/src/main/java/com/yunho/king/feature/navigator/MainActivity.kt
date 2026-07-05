package com.yunho.king.feature.navigator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.yunho.king.core.designsystem.theme.KingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navigatorViewModel: NavigatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigatorViewModel.setIntent(intent)
        setContent {
            KingTheme {
                CameraAudioTrackerNavHost(navigatorViewModel = navigatorViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        navigatorViewModel.setIntent(intent)
    }
}
