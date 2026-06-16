package com.yunho.king.core.designsystem.component

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun KingAppListItem(
    appName: String,
    pkgName: String,
    subtitleLine1: String,
    subtitleLine2: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val rowModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    } else {
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
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
                Text(text = appName, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitleLine1, style = MaterialTheme.typography.bodySmall)
                Text(text = subtitleLine2, style = MaterialTheme.typography.bodySmall)
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}
