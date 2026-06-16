package com.yunho.king.domain.usecase

import com.yunho.king.core.model.AudioAppData
import com.yunho.king.core.model.CameraAppData
import com.yunho.king.core.model.ScannedApp
import com.yunho.king.domain.repository.RepositorySource
import com.yunho.king.domain.source.InstalledAppScannerSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedAppDatabaseUseCaseTest {

    @Test
    fun `seeds apps on first open and marks complete`() = runTest {
        val repository = FakeRepository(firstOpen = true)
        val scanner = FakeScanner(
            cameraApps = listOf(ScannedApp("com.camera.app", "Camera App")),
            audioApps = listOf(ScannedApp("com.audio.app", "Audio App"))
        )

        SeedAppDatabaseUseCase(repository, scanner).invoke()

        assertEquals(1, repository.cameraInserts)
        assertEquals(1, repository.audioInserts)
        assertFalse(repository.firstOpen)
    }

    @Test
    fun `skips seeding when already completed`() = runTest {
        val repository = FakeRepository(firstOpen = false)
        val scanner = FakeScanner(
            cameraApps = listOf(ScannedApp("com.camera.app", "Camera App")),
            audioApps = listOf(ScannedApp("com.audio.app", "Audio App"))
        )

        SeedAppDatabaseUseCase(repository, scanner).invoke()

        assertEquals(0, repository.cameraInserts)
        assertEquals(0, repository.audioInserts)
    }

    @Test
    fun `does not insert existing apps`() = runTest {
        val repository = FakeRepository(
            firstOpen = true,
            existingCamera = setOf("com.camera.app")
        )
        val scanner = FakeScanner(
            cameraApps = listOf(ScannedApp("com.camera.app", "Camera App")),
            audioApps = emptyList()
        )

        SeedAppDatabaseUseCase(repository, scanner).invoke()

        assertEquals(0, repository.cameraInserts)
        assertTrue(repository.firstOpen == false)
    }

    private class FakeScanner(
        private val cameraApps: List<ScannedApp>,
        private val audioApps: List<ScannedApp>
    ) : InstalledAppScannerSource {
        override fun scanCameraApps(): List<ScannedApp> = cameraApps
        override fun scanAudioApps(): List<ScannedApp> = audioApps
    }

    private class FakeRepository(
        var firstOpen: Boolean,
        private val existingCamera: Set<String> = emptySet(),
        private val existingAudio: Set<String> = emptySet()
    ) : RepositorySource {
        var cameraInserts = 0
        var audioInserts = 0

        override fun clearDb() = Unit
        override suspend fun getAppAlim(): Boolean = true
        override suspend fun setAppAlim(enabled: Boolean) = Unit
        override suspend fun getFirstOpenApp(): Boolean = firstOpen
        override suspend fun setFirstOpenApp(isFirst: Boolean) {
            firstOpen = isFirst
        }
        override suspend fun getRemoveList(): Set<String> = emptySet()
        override suspend fun setRemoveList(pkgs: Set<String>) = Unit
        override fun getAllCameraAppList(): List<CameraAppData> = emptyList()
        override fun getCameraAppData(pkgName: String): CameraAppData =
            CameraAppData(appPackageName = pkgName, permState = false)
        override fun getCameraAppPermUseCount(pkgName: String): Int = 0
        override fun deleteCameraApp(pkgName: String) = Unit
        override fun updateCameraAppPermUseCount(pkgName: String, count: Int) = Unit
        override suspend fun existCameraApp(pkgName: String): Boolean = pkgName in existingCamera
        override fun updateLastUseDate(pkgName: String, lastUse: Long) = Unit
        override fun updateCameraNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) = Unit
        override fun getExceptionCameraAppData(): List<CameraAppData>? = emptyList()
        override suspend fun insertCameraApp(data: CameraAppData) {
            cameraInserts++
        }
        override fun deleteAllCamera() = Unit
        override fun getAllAudioAppList(): List<AudioAppData> = emptyList()
        override fun getAudioAppData(pkgName: String): AudioAppData =
            AudioAppData(appPackageName = pkgName, permState = false)
        override fun getAudioAppPermUseCount(pkgName: String): Int = 0
        override fun deleteAudioApp(pkgName: String) = Unit
        override fun updateAudioAppPermUseCount(pkgName: String, count: Int) = Unit
        override suspend fun existAudioApp(pkgName: String): Boolean = pkgName in existingAudio
        override suspend fun insertAudioApp(data: AudioAppData) {
            audioInserts++
        }
        override fun updateAudioNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long) = Unit
        override fun getExceptionAudioAppData(): List<AudioAppData>? = emptyList()
        override fun updateAudioLastUseDate(pkgName: String, lastUse: Long) = Unit
        override fun deleteAllAudio() = Unit
    }
}
