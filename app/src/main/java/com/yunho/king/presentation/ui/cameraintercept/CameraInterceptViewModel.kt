package com.yunho.king.presentation.ui.cameraintercept

import androidx.lifecycle.ViewModel
import com.yunho.king.domain.di.RepositorySource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraInterceptViewModel @Inject constructor(
    private val repo: RepositorySource
): ViewModel() {



}