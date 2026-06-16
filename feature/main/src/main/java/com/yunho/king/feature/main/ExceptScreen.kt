package com.yunho.king.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yunho.king.core.common.DateFormatUtil
import com.yunho.king.core.designsystem.R as DesignR
import com.yunho.king.core.designsystem.component.KingAppListItem

@Composable
fun ExceptScreen(
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
                val list = state.exCameraList
                if (list.isEmpty()) {
                    Text(
                        stringResource(DesignR.string.non_list),
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn {
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
                                trailing = {
                                    Button(
                                        onClick = {
                                            onIntent(
                                                MainContract.Intent.RemoveFromException(
                                                    item.appPackageName,
                                                    true
                                                )
                                            )
                                        }
                                    ) {
                                        Text(stringResource(DesignR.string.remove_exception))
                                    }
                                }
                            )
                        }
                    }
                }
            }
            MainContract.UsageTab.Audio -> {
                val list = state.exAudioList
                if (list.isEmpty()) {
                    Text(
                        stringResource(DesignR.string.non_list),
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn {
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
                                trailing = {
                                    Button(
                                        onClick = {
                                            onIntent(
                                                MainContract.Intent.RemoveFromException(
                                                    item.appPackageName,
                                                    false
                                                )
                                            )
                                        }
                                    ) {
                                        Text(stringResource(DesignR.string.remove_exception))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
