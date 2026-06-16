package com.yunho.king.domain.source

import com.yunho.king.core.model.ScannedApp

interface InstalledAppScannerSource {
    fun scanCameraApps(): List<ScannedApp>
    fun scanAudioApps(): List<ScannedApp>
}
