package com.yunho.king.presentation.ui.intro

import androidx.lifecycle.ViewModel
import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {

}