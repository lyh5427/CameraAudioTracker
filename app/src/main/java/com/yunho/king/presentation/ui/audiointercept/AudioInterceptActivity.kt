package com.yunho.king.presentation.ui.audiointercept

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yunho.king.core.common.Const
import com.yunho.king.core.designsystem.theme.KingTheme
import com.yunho.king.feature.intercept.audio.AudioInterceptScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioInterceptActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pkgName = intent?.getStringExtra(Const.PKG_NAME).orEmpty()

        setContent {
            KingTheme {
                AudioInterceptScreen(
                    pkgName = pkgName,
                    onDismiss = { finish() }
                )
            }
        }
    }
}

