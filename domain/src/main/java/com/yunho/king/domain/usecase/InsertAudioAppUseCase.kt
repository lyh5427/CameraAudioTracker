package com.yunho.king.domain.usecase

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.domain.repository.RepositorySource
import javax.inject.Inject

class InsertAudioAppUseCase @Inject constructor(
    private val repository: RepositorySource
) {
    suspend operator fun invoke(data: AudioAppData) = repository.insertAudioApp(data)
}
