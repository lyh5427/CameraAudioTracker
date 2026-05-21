package com.yunho.king.feature.launch.intro

object IntroContract {
    data class State(val isLoading: Boolean = true)

    sealed interface Intent {
        data object OnStart : Intent
    }

    sealed interface Effect {
        data object NavigateToPerm : Effect
        data object NavigateToMain : Effect
    }
}
