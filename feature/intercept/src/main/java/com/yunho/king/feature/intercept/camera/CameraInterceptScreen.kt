package com.yunho.king.feature.intercept.camera

import android.net.Uri
import android.provider.Settings
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.yunho.king.core.designsystem.R as DesignR
import com.yunho.king.core.designsystem.component.KingPrimaryButton
import com.yunho.king.core.designsystem.component.KingSecondaryButton

@Composable
fun CameraInterceptScreen(
    pkgName: String,
    onDismiss: () -> Unit,
    viewModel: CameraInterceptViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(pkgName) {
        viewModel.onIntent(CameraInterceptContract.Intent.SetPackageName(pkgName))
    }
    LaunchedEffect(state.packageName) {
        if (state.packageName.isNotEmpty()) {
            viewModel.loadAppInfo(context.packageManager)
        }
    }

    var cameraAlim by remember { mutableStateOf(false) }
    var appAlim by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 카메라 프리뷰 영역 (아이콘 대신)
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).also { previewView ->
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().apply {
                                    setSurfaceProvider(previewView.surfaceProvider)
                                }
                                try {
                                    cameraProvider.unbindAll()
                                    cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_FRONT_CAMERA,
                                        preview
                                    )
                                } catch (_: Exception) {
                                }
                            }, ContextCompat.getMainExecutor(ctx))
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 타이틀 / 설명
            Text(
                text = stringResource(DesignR.string.suspicion_popup_camera_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    DesignR.string.intercept_camera_desc,
                    state.appName.ifEmpty { pkgName }
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // 알림 옵션
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Checkbox(checked = cameraAlim, onCheckedChange = { cameraAlim = it })
                Text(stringResource(DesignR.string.now_app_alim_off), style = MaterialTheme.typography.bodyMedium)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Checkbox(checked = appAlim, onCheckedChange = { appAlim = it })
                Text(stringResource(DesignR.string.today_off_alim), style = MaterialTheme.typography.bodyMedium)
            }

            // 하단 버튼 2개 (시스템 UI에 가려지지 않도록 navigationBarsPadding 사용)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KingPrimaryButton(
                    text = stringResource(DesignR.string.ok),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.onIntent(CameraInterceptContract.Intent.SetAlim(cameraAlim, appAlim))
                        onDismiss()
                    }
                )
                KingSecondaryButton(
                    text = stringResource(DesignR.string.setting),
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
                )
            }
        }
    }
}
