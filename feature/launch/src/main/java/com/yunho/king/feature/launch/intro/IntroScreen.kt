package com.yunho.king.feature.launch.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yunho.king.core.designsystem.R as DesignR
import com.yunho.king.core.designsystem.component.KingPrimaryButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IntroScreen(
    onNavigateToPerm: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: IntroViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(IntroContract.Intent.OnStart)
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is IntroContract.Effect.NavigateToPerm -> onNavigateToPerm()
                is IntroContract.Effect.NavigateToMain -> onNavigateToMain()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = DesignR.drawable.img_splash),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(DesignR.string.intro_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(DesignR.string.intro_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            KingPrimaryButton(
                text = stringResource(DesignR.string.intro_start),
                onClick = onNavigateToPerm
            )
        }
    }
}
