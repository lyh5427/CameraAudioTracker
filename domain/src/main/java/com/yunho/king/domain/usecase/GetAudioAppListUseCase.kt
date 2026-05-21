package com.yunho.king.domain.usecase

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.domain.repository.RepositorySource
import javax.inject.Inject

class GetAudioAppListUseCase @Inject constructor(
    private val repository: RepositorySource
) {
    operator fun invoke(): List<AudioAppData> = repository.getAllAudioAppList()
}
