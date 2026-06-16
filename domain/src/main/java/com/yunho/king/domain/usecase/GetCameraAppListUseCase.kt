package com.yunho.king.domain.usecase

import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import javax.inject.Inject

class GetCameraAppListUseCase @Inject constructor(
    private val repository: RepositorySource
) {
    operator fun invoke(): List<CameraAppData> = repository.getAllCameraAppList()
}
