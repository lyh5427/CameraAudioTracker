package com.yunho.king.feature.intercept.audio

import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AudioInterceptScreen(
    pkgName: String,
    onDismiss: () -> Unit,
    viewModel: AudioInterceptViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(pkgName) {
        viewModel.onIntent(AudioInterceptContract.Intent.SetPackageName(pkgName))
    }
    LaunchedEffect(Unit) {
        viewModel.setAppInfo(context.packageManager)
    }

    var audioAlim by remember { mutableStateOf(false) }
    var appAlim by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "오디오 사용 감지",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                state.appIcon?.let { drawable ->
                    AndroidView(
                        factory = { ctx ->
                            android.widget.ImageView(ctx).apply { setImageDrawable(drawable) }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(top = 4.dp)
                    )
                }
                Text(
                    text = state.appName.ifEmpty { pkgName },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Checkbox(checked = audioAlim, onCheckedChange = { audioAlim = it })
                    Text("이 앱 알림 끄기", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = appAlim, onCheckedChange = { appAlim = it })
                    Text("앱 전체 알림 끄기", style = MaterialTheme.typography.bodyMedium)
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.onIntent(AudioInterceptContract.Intent.SetAlim(audioAlim, appAlim))
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("닫기")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            context.startActivity(
                                android.content.Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:$pkgName")
                                )
                            )
                            onDismiss()
                        }
                    ) {
                        Text("앱 설정")
                    }
                }
            }
        }
    }
}
