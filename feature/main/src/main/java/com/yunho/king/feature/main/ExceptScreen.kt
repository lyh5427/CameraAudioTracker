package com.yunho.king.feature.main

import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yunho.king.core.common.DateFormatUtil

@Composable
fun ExceptScreen(
    state: MainContract.State,
    onIntent: (MainContract.Intent) -> Unit
) {
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
                val list = state.exCameraList
                if (list.isEmpty()) {
                    Text("제외 앱 없음", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(list, key = { it.appPackageName }) { item ->
                            ExListItem(
                                appName = item.appName,
                                pkgName = item.appPackageName,
                                permUseCount = item.permUseCount,
                                lastUseDateTime = item.lastUseDateTime,
                                onRemove = { onIntent(MainContract.Intent.RemoveFromException(item.appPackageName, true)) }
                            )
                        }
                    }
                }
            }
            MainContract.UsageTab.Audio -> {
                val list = state.exAudioList
                if (list.isEmpty()) {
                    Text("제외 앱 없음", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        items(list, key = { it.appPackageName }) { item ->
                            ExListItem(
                                appName = item.appName,
                                pkgName = item.appPackageName,
                                permUseCount = item.permUseCount,
                                lastUseDateTime = item.lastUseDateTime,
                                onRemove = { onIntent(MainContract.Intent.RemoveFromException(item.appPackageName, false)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExListItem(
    appName: String,
    pkgName: String,
    permUseCount: Int,
    lastUseDateTime: Long,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        Button(onClick = onRemove) { Text("제외 해제") }
    }
}
