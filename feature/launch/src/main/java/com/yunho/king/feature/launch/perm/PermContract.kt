package com.yunho.king.feature.launch.perm

object PermContract {
    data class State(
        val permissionsGranted: Boolean = false,
        val isLoading: Boolean = false
    )

    sealed interface Intent {
        data object RequestPermissions : Intent
        data class PermissionsResult(val granted: Map<String, Boolean>) : Intent
        data object SkipToMain : Intent
    }

    sealed interface Effect {
        data object NavigateToMain : Effect
        data object OpenUsageStatsSettings : Effect
        data object OpenOverlaySettings : Effect
    }
}
