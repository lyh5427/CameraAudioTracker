package com.yunho.king.feature.main.settings

object SettingsContract {
    data class State(
        val appAlimEnabled: Boolean = true,
        val isLoading: Boolean = true
    )

    sealed interface Intent {
        data object Load : Intent
        data class SetAppAlim(val enabled: Boolean) : Intent
    }
}
