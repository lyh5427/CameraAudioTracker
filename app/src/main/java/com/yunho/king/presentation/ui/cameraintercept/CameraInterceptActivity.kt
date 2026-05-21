package com.yunho.king.presentation.ui.cameraintercept

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yunho.king.core.common.Const
import com.yunho.king.core.designsystem.theme.KingTheme
import com.yunho.king.feature.intercept.camera.CameraInterceptScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraInterceptActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pkgName = intent?.getStringExtra(Const.PKG_NAME).orEmpty()

        setContent {
            KingTheme {
                CameraInterceptScreen(
                    pkgName = pkgName,
                    onDismiss = { finish() }
                )
            }
        }
    }
}

