# King (CameraAudioTracker)

기기에서 **카메라·마이크 권한을 사용하는 앱**을 실시간으로 감지하고, 사용 이력을 추적하는 Android 보안 앱입니다.

| 항목 | 값 |
|------|-----|
| 패키지명 | `com.yunho.king` |
| minSdk | 26 |
| targetSdk | 35 |
| compileSdk | 36 |
| version | 1.0.0.0 (14) |
| JDK | **17** (필수) |

---

## 주요 기능

- **실시간 감지**: 포그라운드 서비스에서 카메라/마이크 사용 모니터링
- **인터셉트 알림**: 타 앱 사용 감지 시 Compose 팝업 표시 (CameraX 프리뷰 포함)
- **사용 이력**: Room DB에 앱별 사용 횟수·최근 사용 시각 저장
- **제외 관리**: 앱별 / 전체 알림 끄기, 제외 목록 탭
- **앱 상세**: 패키지별 권한·사용 통계 조회
- **설치 감지**: 앱 설치/삭제 시 DB 자동 갱신

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 2.2 |
| UI | Jetpack Compose, Material 3 |
| Architecture | Clean Architecture, MVI |
| DI | Hilt (KSP) |
| DB | Room, DataStore |
| Navigation | Navigation Compose |
| Camera | CameraX |
| Ads / Analytics | AdMob, Firebase (BOM) |
| Build | Gradle Kotlin DSL, Version Catalog, Convention Plugins |

---

## 모듈 구조

```
CameraAudioTracker/
├── app/                 # Application, MainService, InstallReceiver
├── build-logic/         # king.android.* Convention Plugins
├── core/
│   ├── model/           # Room Entity, ScannedApp
│   ├── common/          # MVI, PermManager, Const, AdMobUtil
│   └── designsystem/    # KingTheme, KingButton, KingAppListItem
├── data/                # Room, RepositoryImpl, InstalledAppScanner
├── domain/              # RepositorySource, UseCase
└── feature/
    ├── launch/          # Intro, Perm
    ├── navigator/       # MainActivity, NavHost
    ├── main/            # Usage, Except, Hole, Settings
    ├── intercept/       # Camera/Audio Intercept
    └── appdetail/       # App Detail
```

---

## 사전 요구 사항

- **JDK 17** (Gradle 런타임·컴파일 모두)
  - 시스템 기본이 JDK 26이면 빌드가 실패할 수 있습니다.
  - `./gradlew`는 Android Studio JBR 또는 `local.properties`의 `java.home`을 자동으로 우선 사용합니다.
- Android SDK (API 36 compile)
- Android Studio (권장: 최신 Stable)

---

## 프로젝트 설정

### 1. `local.properties`

```bash
cp local.properties.example local.properties
```

필수 항목을 채웁니다.

```properties
sdk.dir=/Users/YOUR_USER/Library/Android/sdk

# JDK 26+ 환경이면 권장
java.home=/Applications/Android Studio.app/Contents/jbr/Contents/Home

ADMOB_ID=ca-app-pub-xxxxxxxx~xxxxxxxx
ADMOB_UNIT_ID_MAIN_BANNER=ca-app-pub-xxxxxxxx/xxxxxxxxxx

RELEASE_KEY_PATH=...
RELEASE_KEY_ALIAS=...
RELEASE_KEY_PW=...
DEBUG_KEY_PATH=...
DEBUG_KEY_ALIAS=...
DEBUG_KEY_PW=...
```

### 2. `google-services.json`

Firebase / Crashlytics용 파일을 `app/` 디렉터리에 배치합니다. (gitignore 대상)

### 3. 트래킹 매니저 (로컬 전용)

아래 파일은 보안상 git에 포함되지 않습니다. 로컬에 반드시 존재해야 합니다.

- `app/src/main/java/com/yunho/king/presentation/service/CameraTrackingManager.kt`
- `app/src/main/java/com/yunho/king/presentation/service/AudioTrackingManager.kt`

감지 후 인터셉트 화면은 `InterceptNavigation` → `MainActivity` → Compose NavHost 경로로 열립니다.

---

## 빌드 & 실행

```bash
# 단위 테스트 (domain)
./gradlew :domain:test

# Debug APK
./gradlew :app:assembleDebug

# 기기 설치
./gradlew :app:installDebug

# Instrumented test (기기/에뮬레이터 연결)
./gradlew :app:connectedDebugAndroidTest
```

Android Studio: **Run** ▶ → `app` 모듈 실행

Gradle JDK 설정: **Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK → 17**

---

## 화면 흐름

```
Intro → Perm (권한) → Main
                         ├── 사용 정보 (Usage)
                         ├── 제외 목록 (Except)
                         └── 전체 (Hole)
Main → App Detail
Main → Settings (전체 알림 on/off)

[백그라운드] MainService 감지 → InterceptNavigation → Camera/Audio Intercept
```

---

## 아키텍처 요약

### 레이어

```
feature → domain ← data
              ↑
           core (model, common, designsystem)
```

### MVI 패턴

Compose 화면은 `Contract (State / Intent / Effect)` + `ViewModel (mviIntentStore)` + `Screen` 구조를 따릅니다.

### 데이터

| 저장소 | 용도 |
|--------|------|
| Room (`ca.db`, `ad.db`) | 카메라/오디오 앱 목록, 사용 이력 |
| DataStore (`king_prefs`) | 전체 알림, 제외 목록, 최초 실행 플래그 |

최초 실행 시 `SeedAppDatabaseUseCase`가 설치된 앱을 스캔해 DB를 초기화합니다.

---

## 권한

| 권한 | 용도 |
|------|------|
| `CAMERA` | 감지, 인터셉트 프리뷰 |
| `RECORD_AUDIO` | 마이크 사용 감지 |
| `PACKAGE_USAGE_STATS` | 사용 중 앱 식별 |
| `SYSTEM_ALERT_WINDOW` | 오버레이 알림 |
| `POST_NOTIFICATIONS` | 포그라운드 서비스 알림 |
| `FOREGROUND_SERVICE_CAMERA` / `MICROPHONE` | FGS 타입 |

---

## Convention Plugins

`build-logic/`에서 제공하는 플러그인:

| Plugin ID | 용도 |
|-----------|------|
| `king.android.application` | app 모듈 |
| `king.android.application.compose` | Compose + app |
| `king.android.library` | 라이브러리 |
| `king.android.library.compose` | Compose 라이브러리 |
| `king.android.feature` | feature 모듈 (Compose + Hilt + Nav) |
| `king.hilt` | Hilt 설정 |

---

## 테스트

| 종류 | 위치 |
|------|------|
| Unit | `domain/src/test/` — `SeedAppDatabaseUseCaseTest` 등 |
| Instrumented | `app/src/androidTest/` — `MainActivityLaunchTest` |

---

## 트러블슈팅

### `java.lang.IllegalArgumentException: 26`

Gradle이 JDK 26으로 실행된 경우입니다.

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleDebug
```

또는 `local.properties`에 `java.home`을 설정하세요.

### 인터셉트 팝업이 안 뜸

1. 메인 화면까지 진입했는지 확인 (`MainService` 시작 시점)
2. 설정 → **전체 알림**이 켜져 있는지 확인
3. `CameraTrackingManager.kt` / `AudioTrackingManager.kt` 로컬 파일 존재 여부 확인

### DB가 비어 있음

앱 최초 실행 시 Intro에서 `SeedAppDatabaseUseCase`가 동작합니다. **전체** 탭에서 목록을 확인하세요.

---

## 라이선스

Private project. 별도 라이선스 명시 전까지 내부용으로 관리합니다.
