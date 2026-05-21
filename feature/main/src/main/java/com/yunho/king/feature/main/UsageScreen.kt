package com.yunho.king.feature.main

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yunho.king.core.common.DateFormatUtil

@Composable
fun UsageScreen(
    state: MainContract.State,
    onIntent: (MainContract.Intent) -> Unit
) {
    val context = LocalContext.current
    val selectedTab = state.selectedUsageTab

    Column(Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == MainContract.UsageTab.Camera,
                onClick = { onIntent(MainContract.Intent.SelectUsageTab(MainContract.UsageTab.Camera)) },
                text = { Text("카메라") }
            )
            Tab(
                selected = selectedTab == MainContract.UsageTab.Audio,
                onClick = { onIntent(MainContract.Intent.SelectUsageTab(MainContract.UsageTab.Audio)) },
                text = { Text("오디오") }
            )
        }
        when (selectedTab) {
            MainContract.UsageTab.Camera -> {
                LaunchedEffect(state.currentCameraUsagePage) {
                    if (state.cameraUsageList.isEmpty() && state.cameraUsagePageCount > 0) {
                        onIntent(MainContract.Intent.LoadCameraPage(1))
                    }
                }
                val list = state.cameraUsageFiltered
                if (list.isEmpty()) {
                    Text("목록 없음", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn(state = rememberLazyListState()) {
                        items(list, key = { it.appPackageName }) { item ->
                            UsageListItem(
                                appName = item.appName,
                                pkgName = item.appPackageName,
                                permUseCount = item.permUseCount,
                                lastUseDateTime = item.lastUseDateTime,
                                context = context,
                                onClick = { onIntent(MainContract.Intent.NavigateToDetail(item.appPackageName)) }
                            )
                        }
                    }
                    if (state.cameraUsagePageCount > 1) {
                        Row(
                            Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            (1..state.cameraUsagePageCount).forEach { page ->
                                Text(
                                    text = "$page",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable { onIntent(MainContract.Intent.LoadCameraPage(page)) },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            MainContract.UsageTab.Audio -> {
                LaunchedEffect(state.currentAudioUsagePage) {
                    if (state.audioUsageList.isEmpty() && state.audioUsagePageCount > 0) {
                        onIntent(MainContract.Intent.LoadAudioPage(1))
                    }
                }
                val list = state.audioUsageFiltered
                if (list.isEmpty()) {
                    Text("목록 없음", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn(state = rememberLazyListState()) {
                        items(list, key = { it.appPackageName }) { item ->
                            UsageListItem(
                                appName = item.appName,
                                pkgName = item.appPackageName,
                                permUseCount = item.permUseCount,
                                lastUseDateTime = item.lastUseDateTime,
                                context = context,
                                onClick = { onIntent(MainContract.Intent.NavigateToDetail(item.appPackageName)) }
                            )
                        }
                    }
                    if (state.audioUsagePageCount > 1) {
                        Row(
                            Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            (1..state.audioUsagePageCount).forEach { page ->
                                Text(
                                    text = "$page",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable { onIntent(MainContract.Intent.LoadAudioPage(page)) },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UsageListItem(
    appName: String,
    pkgName: String,
    permUseCount: Int,
    lastUseDateTime: Long,
    context: android.content.Context,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    try {
                        setImageDrawable(ctx.packageManager.getApplicationIcon(pkgName))
                    } catch (_: Exception) { }
                }
            },
            modifier = Modifier.size(48.dp)
        )
        Column(Modifier.padding(start = 16.dp)) {
            Text(text = appName.ifEmpty { "앱" }, style = MaterialTheme.typography.titleMedium)
            Text(text = "사용 횟수: $permUseCount", style = MaterialTheme.typography.bodySmall)
            Text(text = "최근 사용: ${DateFormatUtil.format(lastUseDateTime)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
