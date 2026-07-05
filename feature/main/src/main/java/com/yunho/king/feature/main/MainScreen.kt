package com.yunho.king.feature.main

import android.util.Log
import com.yunho.king.core.common.AdMobUtil
import com.yunho.king.core.common.PermManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.IconButton
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.yunho.king.core.designsystem.R as DesignR
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToAppDetail: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val permManager = remember { PermManager(context) }
    LaunchedEffect(Unit) {
        viewModel.onIntent(MainContract.Intent.LoadUsageData)
        viewModel.onIntent(MainContract.Intent.LoadExceptionData)
        if (!permManager.isRuntimePermAllow()) return@LaunchedEffect
        val serviceIntent = Intent().setClassName(context, "com.yunho.king.presentation.service.MainService")
        try {
            context.startForegroundService(serviceIntent)
        } catch (e: SecurityException) {
            Log.e("King", "Failed to start MainService", e)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is MainContract.Effect.NavigateToAppDetail -> onNavigateToAppDetail(effect.pkgName)
                is MainContract.Effect.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(DesignR.string.main_title),
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(MainContract.Intent.OpenSettings) }) {
                        Icon(
                            painter = painterResource(DesignR.drawable.icon_detail),
                            contentDescription = stringResource(DesignR.string.setting)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        bottomBar = {
            Column {
                AndroidView(
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                            adUnitId = AdMobUtil.getMainBannerUnitId(ctx)
                            adListener = object : com.google.android.gms.ads.AdListener() {
                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Log.e("King", error.message)
                                }
                            }
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                NavigationBar {
                    listOf(
                        Triple(MainContract.MainTab.Usage, DesignR.string.navi_usages, DesignR.drawable.icon_usage),
                        Triple(MainContract.MainTab.Except, DesignR.string.navi_except, DesignR.drawable.icon_exception),
                        Triple(MainContract.MainTab.Hole, DesignR.string.navi_hole, DesignR.drawable.icon_hole)
                    ).forEach { (tab, labelRes, iconRes) ->
                        val label = stringResource(labelRes)
                        NavigationBarItem(
                            selected = state.selectedMainTab == tab,
                            onClick = { viewModel.onIntent(MainContract.Intent.SelectMainTab(tab)) },
                            icon = { Icon(painterResource(iconRes), contentDescription = label) },
                            label = { Text(label) },
                            colors = NavigationBarItemDefaults.colors()
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(Modifier.weight(1f)) {
                when (state.selectedMainTab) {
                    MainContract.MainTab.Usage -> UsageScreen(
                        state = state,
                        onIntent = viewModel::onIntent
                    )
                    MainContract.MainTab.Except -> ExceptScreen(
                        state = state,
                        onIntent = viewModel::onIntent
                    )
                    MainContract.MainTab.Hole -> HoleScreen(
                        state = state,
                        onIntent = viewModel::onIntent
                    )
                }
            }
        }
    }
}
