package com.yunho.king.feature.appdetail

import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.yunho.king.core.common.DateFormatUtil
import com.yunho.king.core.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    pkgName: String,
    onBack: () -> Unit,
    viewModel: AppDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(pkgName) {
        viewModel.onIntent(AppDetailContract.Intent.Load(pkgName))
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(DesignR.string.app_detail_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(DesignR.string.back)
                    )
                }
            }
        )
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AndroidView(
                        factory = { ctx ->
                            ImageView(ctx).apply {
                                try {
                                    setImageDrawable(ctx.packageManager.getApplicationIcon(state.packageName))
                                } catch (_: Exception) {
                                }
                            }
                        },
                        modifier = Modifier.size(64.dp)
                    )
                    Column(Modifier.padding(start = 16.dp)) {
                        Text(
                            text = state.appName.ifEmpty { state.packageName },
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = state.packageName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                state.cameraData?.let { camera ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(DesignR.string.app_detail_camera_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_state_title,
                                    if (camera.permState) {
                                        stringResource(DesignR.string.status_allow)
                                    } else {
                                        stringResource(DesignR.string.status_denied)
                                    }
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_count_title,
                                    camera.permUseCount.toString()
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_last_date_title,
                                    DateFormatUtil.format(camera.lastUseDateTime)
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (state.audioData != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                state.audioData?.let { audio ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(DesignR.string.app_detail_audio_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_state_title,
                                    if (audio.permState) {
                                        stringResource(DesignR.string.status_allow)
                                    } else {
                                        stringResource(DesignR.string.status_denied)
                                    }
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_count_title,
                                    audio.permUseCount.toString()
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    DesignR.string.perm_usage_last_date_title,
                                    DateFormatUtil.format(audio.lastUseDateTime)
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (state.cameraData == null && state.audioData == null) {
                    Text(
                        stringResource(DesignR.string.no_data),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
