package com.yunho.king.domain.di

import android.graphics.Camera
import com.yunho.king.domain.dto.AudioAppData
import com.yunho.king.domain.dto.CameraAppData

interface RepositorySource {
    fun clearDb()

    // Camera DB

    /** DB에 저장된 모든 카메라 앱 리스트를 가져 온다 */
    fun getAllCameraAppList(): List<CameraAppData>

    /** 매개 변수 패키지명을 가진 앱의 저장된 CameraAppData를 가져 온다*/
    fun getCameraAppData(pkgName: String): CameraAppData

    /** 매개 변수 패키지명을 가진 앱의 카메라 사용 횟수를 가져 온다 */
    fun getCameraAppPermUseCount(pkgName: String): Int

    /** 매개 변수 패키지명을 가진 앱의 데이터를 삭제 한다 */
    fun deleteCameraApp(pkgName: String)

    /** 매개 변수 패키지명을 가진 앱의 권한 사용 횟수를 업데이트 한다.*/
    fun updateCameraAppPermUseCount(pkgName: String, count: Int)

    /** 매개 변수 패키지명을 가진 앱의 존재 유무를 반환 한다 */
    suspend fun existCameraApp(pkgName: String): Boolean

    /** 매개 변수 패키지명을 가진 앱의 마지막 카메라 사용 날짜를 업데이트 한다.*/
    fun updateLastUseDate(pkgName: String, lastUse: Long)

    /** 매개 변수 패키지명의 알림 플래그를 업데이트 한다. */
    fun updateCameraNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long)

    /** 알림에서 제외된 모든 카메라 앱 데이터를 리스트로 가져 온다. */
    fun getExceptionCameraAppData(): List<CameraAppData>?

    /** 카메라 앱 데이터를 저장한다. */
    suspend fun insertCameraApp(data: CameraAppData)

    /** 모든 카메라 앱 데이터를 삭제 한다.*/
    fun deleteAllCamera()

    // Audio DB
    /** DB에 저장된 모든 오디오 앱 리스트를 가져 온다 */
    fun getAllAudioAppList(): List<AudioAppData>

    /** 매개 변수 패키지명을 가진 앱의 저장된 AudioAppData를 가져 온다*/
    fun getAudioAppData(pkgName: String): AudioAppData

    /** 매개 변수 패키지명을 가진 앱의 오디오 사용 횟수를 가져 온다 */
    fun getAudioAppPermUseCount(pkgName: String): Int

    /** 매개 변수 패키지명을 가진 앱의 데이터를 삭제 한다 */
    fun deleteAudioApp(pkgName: String)

    /** 매개 변수 패키지명을 가진 앱의 권한 사용 횟수를 업데이트 한다.*/
    fun updateAudioAppPermUseCount(pkgName: String, count: Int)

    /** 매개 변수 패키지명을 가진 앱의 존재 유무를 반환 한다 */
    suspend fun existAudioApp(pkgName: String): Boolean

    /** 매개 변수 패키지명을 가진 앱의 마지막 오디오 사용 날짜를 업데이트 한다.*/
    suspend fun insertAudioApp(data: AudioAppData)

    /** 매개 변수 패키지명의 알림 플래그를 업데이트 한다. */
    fun updateAudioNotiFlag(pkgName: String, notiFlag: Boolean, exceptionDate: Long)

    /** 알림에서 제외된 모든 카메라 앱 데이터를 리스트로 가져 온다. */
    fun getExceptionAudioAppData(): List<AudioAppData>?

    /** 모든 카메라 앱 데이터를 삭제 한다.*/
    fun deleteAllAudio()
}