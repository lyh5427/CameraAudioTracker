package com.yunho.king.feature.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yunho.king.core.common.DateFormatUtil
import com.yunho.king.core.designsystem.R as DesignR
import com.yunho.king.core.designsystem.component.KingAppListItem

@Composable
fun UsageScreen(
    state: MainContract.State,
    onIntent: (MainContract.Intent) -> Unit
) {
    val selectedTab = state.selectedUsageTab
    val unknownApp = stringResource(DesignR.string.un_known)

    Column(Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == MainContract.UsageTab.Camera,
                onClick = { onIntent(MainContract.Intent.SelectUsageTab(MainContract.UsageTab.Camera)) },
                text = { Text(stringResource(DesignR.string.camera_tab)) }
            )
            Tab(
                selected = selectedTab == MainContract.UsageTab.Audio,
                onClick = { onIntent(MainContract.Intent.SelectUsageTab(MainContract.UsageTab.Audio)) },
                text = { Text(stringResource(DesignR.string.audio_tab)) }
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
                    Text(
                        stringResource(DesignR.string.empty_list),
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(state = rememberLazyListState()) {
                        items(list, key = { it.appPackageName }) { item ->
                            KingAppListItem(
                                appName = item.appName.ifEmpty { unknownApp },
                                pkgName = item.appPackageName,
                                subtitleLine1 = stringResource(
                                    DesignR.string.app_list_count,
                                    item.permUseCount.toString()
                                ),
                                subtitleLine2 = stringResource(
                                    DesignR.string.app_list_last_use,
                                    DateFormatUtil.format(item.lastUseDateTime)
                                ),
                                onClick = {
                                    onIntent(MainContract.Intent.NavigateToDetail(item.appPackageName))
                                }
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
                    Text(
                        stringResource(DesignR.string.empty_list),
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(state = rememberLazyListState()) {
                        items(list, key = { it.appPackageName }) { item ->
                            KingAppListItem(
                                appName = item.appName.ifEmpty { unknownApp },
                                pkgName = item.appPackageName,
                                subtitleLine1 = stringResource(
                                    DesignR.string.app_list_count,
                                    item.permUseCount.toString()
                                ),
                                subtitleLine2 = stringResource(
                                    DesignR.string.app_list_last_use,
                                    DateFormatUtil.format(item.lastUseDateTime)
                                ),
                                onClick = {
                                    onIntent(MainContract.Intent.NavigateToDetail(item.appPackageName))
                                }
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
