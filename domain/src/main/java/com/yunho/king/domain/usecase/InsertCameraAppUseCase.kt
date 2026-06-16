package com.yunho.king.domain.usecase

import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import javax.inject.Inject

class InsertCameraAppUseCase @Inject constructor(
    private val repository: RepositorySource
) {
    suspend operator fun invoke(data: CameraAppData) = repository.insertCameraApp(data)
}
