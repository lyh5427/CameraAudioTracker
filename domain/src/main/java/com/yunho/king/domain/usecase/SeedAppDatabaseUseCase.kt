package com.yunho.king.domain.usecase

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.domain.repository.RepositorySource
import com.yunho.king.domain.source.InstalledAppScannerSource
import javax.inject.Inject

class SeedAppDatabaseUseCase @Inject constructor(
    private val repository: RepositorySource,
    private val scanner: InstalledAppScannerSource
) {
    suspend operator fun invoke() {
        if (!repository.getFirstOpenApp()) return

        scanner.scanCameraApps().forEach { app ->
            if (!repository.existCameraApp(app.packageName)) {
                repository.insertCameraApp(
                    CameraAppData(
                        appPackageName = app.packageName,
                        appName = app.appName,
                        permState = false
                    )
                )
            }
        }

        scanner.scanAudioApps().forEach { app ->
            if (!repository.existAudioApp(app.packageName)) {
                repository.insertAudioApp(
                    AudioAppData(
                        appPackageName = app.packageName,
                        appName = app.appName,
                        permState = false
                    )
                )
            }
        }

        repository.setFirstOpenApp(false)
    }
}
