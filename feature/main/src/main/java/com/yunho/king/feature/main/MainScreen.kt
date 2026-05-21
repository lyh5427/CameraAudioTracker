package com.yunho.king.feature.main

import android.util.Log
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
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToAppDetail: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.onIntent(MainContract.Intent.LoadUsageData)
        viewModel.onIntent(MainContract.Intent.LoadExceptionData)
        val serviceIntent = Intent().setClassName(context, "com.yunho.king.presentation.service.MainService")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is MainContract.Effect.NavigateToAppDetail -> onNavigateToAppDetail(effect.pkgName)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "King 보안 모니터링",
                        style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: 설정 진입 등 확장 가능 */ }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null
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
                            adUnitId = "ca-app-pub-3940256099942544/6300978111"
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
                        Triple(MainContract.MainTab.Usage, "사용 현황", Icons.Filled.List),
                        Triple(MainContract.MainTab.Except, "제외 앱", Icons.Filled.Star)
                    ).forEach { (tab, label, icon) ->
                        NavigationBarItem(
                            selected = state.selectedMainTab == tab,
                            onClick = { viewModel.onIntent(MainContract.Intent.SelectMainTab(tab)) },
                            icon = { Icon(icon, contentDescription = label) },
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
                }
            }
        }
    }
}
