package com.yunho.king.presentation.ui.main

import com.yunho.king.domain.di.RepositorySource
import com.yunho.king.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: RepositorySource
): BaseViewModel(repo) {
}