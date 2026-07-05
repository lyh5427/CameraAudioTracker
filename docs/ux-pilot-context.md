# King (CameraAudioTracker) — UX Pilot Design Context

> UX Pilot PRD / Autoflow / Screen generation용 컨텍스트 문서  
> 플랫폼: **Android Mobile** (minSdk 26, Material 3, Jetpack Compose)  
> 언어: **한국어 UI** (Primary), 앱명 **King**

---

## 1. Product Overview

### What is King?

Android 보안 앱. 다른 앱이 **카메라·마이크를 사용하는 순간**을 백그라운드에서 감지하고, 사용자에게 **즉시 알림(인터셉트 화면)**을 띄운다. 감지 이력은 DB에 저장되어 앱별 사용 횟수·최근 사용 시각을 확인할 수 있다.

### Target User

- 스마트폰에서 프라이버시·보안에 민감한 사용자
- "내 폰 카메라/마이크가 언제 켜지는지" 알고 싶은 사용자
- 기술에 익숙한 한국어 사용자 (20–40대)

### Core Value Proposition

- **실시간 감지**: 포그라운드 서비스로 24/7 모니터링
- **투명한 이력**: 어떤 앱이 몇 번 사용했는지 기록
- **즉각 대응**: 의심 시 앱 설정으로 바로 이동 가능

### Tone & Personality

- **신뢰감**, **경계**, **명확함** — 과도하게 겁주지 않되 보안 앱다운 진지함
- 문장은 짧고 행동 유도가 분명해야 함
- 공포 마케팅 X → "감지했습니다", "확인해 주세요" 톤

---

## 2. Design System (King Theme)

### Color Palette (Dark-first, Navy + Blue)


| Token         | Hex       | Usage              |
| ------------- | --------- | ------------------ |
| Background    | `#050A12` | 전체 화면 배경 (다크 네이비)  |
| Surface       | `#111926` | 카드, 시트, 리스트 아이템 배경 |
| Primary       | `#2F7DFF` | CTA 버튼, 강조, 브랜드 블루 |
| On Primary    | `#FFFFFF` | Primary 위 텍스트      |
| On Background | `#E5EEF8` | 본문·타이틀 (밝은 블루그레이)  |
| On Surface    | `#C5D0E0` | 보조 텍스트             |
| Outline       | `#304366` | 구분선, 테두리           |


**Accent (레거시·경고용, 선택적)**

- Alert Red: `#FF3333` — 카메라/마이크 감지 강조 시

### Typography

- **Font**: Noto Sans KR (전 weight)
- Display/Headline: Bold 26–32sp
- Title: Bold/Medium 18–22sp
- Body: Regular 14–16sp
- Caption: 12sp

---

## 3. Information Architecture & User Flow

```
[App Launch]
    │
    ▼
┌─────────────┐     권한 미완료      ┌─────────────┐
│   Intro     │ ─────────────────► │    Perm     │
│  (온보딩)    │                    │  (권한 요청)   │
└─────────────┘                    └─────────────┘
    │ 권한 완료                           │
    └──────────────┬────────────────────┘
                   ▼
            ┌─────────────┐
            │    Main     │◄─── 백그라운드 서비스 시작
            │  (3 Bottom  │
            │    Tabs)    │
            └─────────────┘
                   │
      ┌────────────┼────────────┐
      ▼            ▼            ▼
  App Detail   Settings    (탭 전환)
                   │
[Background] 다른 앱 카메라/마이크 사용 감지
                   ▼
      Camera Intercept / Audio Intercept (풀스크린 or 바텀시트)
```

---

## 4. Screen Inventory (9 screens)

### 4.1 Intro (온보딩)

**Purpose**: 앱 가치 전달 + 진입


| Element     | Content                                           |
| ----------- | ------------------------------------------------- |
| Hero image  | Splash/branding image (security shield style)     |
| Title       | `Camera & Audio Tracker`                          |
| Description | 카메라와 마이크 사용을 실시간으로 감지하고, 의심스러운 사용 이력을 한 눈에 확인하세요. |
| CTA         | `시작하기` (Primary, full width)                      |


**Behavior**: 권한이 이미 모두 허용되면 Perm 건너뛰고 Main으로 자동 이동

---

### 4.2 Perm (권한 설정)

**Purpose**: 필수 권한 안내 및 요청


| Element       | Content                                 |
| ------------- | --------------------------------------- |
| Card title    | `권한 허용이 필요합니다`                          |
| Card body     | 카메라, 마이크, 사용량 접근 권한을 허용해야 앱이 제대로 동작합니다. |
| Primary CTA   | `필수 권한 요청`                              |
| Secondary CTA | `나중에 설정`                                |


**Required permissions** (별도 상세 UI로 확장 가능):

- 알림 (POST_NOTIFICATIONS)
- 카메라 — 타 앱 카메라 사용 감지
- 마이크 — 타 앱 마이크 사용 감지
- 다른 앱 위에 그리기 — 백그라운드 팝업
- 사용 정보 접근 — 용의 앱 식별

**Design note**: 권한별 아이콘 + 설명 리스트 형태로 고도화 여지 있음

---

### 4.3 Main Shell

**Purpose**: 앱 허브, 3탭 + 상단바 + 광고 배너


| Element          | Content                  |
| ---------------- | ------------------------ |
| Top bar title    | `King 보안 모니터링`           |
| Top bar action   | 설정 아이콘 (→ Settings)      |
| Ad banner        | 하단 네비 위 배너 영역 (320×50)   |
| Bottom nav Tab 1 | `사용 정보` (icon_usage)     |
| Bottom nav Tab 2 | `제외 목록` (icon_exception) |
| Bottom nav Tab 3 | `전체` (icon_hole)         |


각 탭 내부에 **서브 탭**: `카메라 앱` | `오디오 앱`

---

### 4.4 Usage Tab (사용 정보)

**Purpose**: 알림이 켜진 앱의 **감지 이력** (notiFlag=true)


| Element     | Content                          |
| ----------- | -------------------------------- |
| Sub tabs    | 카메라 앱 / 오디오 앱                    |
| List item   | 앱 아이콘, 앱 이름, 사용 횟수: N, 마지막 사용 날짜 |
| Empty state | `목록 없음`                          |
| Pagination  | 페이지 번호 (10개씩)                    |
| Tap item    | → App Detail                     |


---

### 4.5 Except Tab (제외 목록)

**Purpose**: 알림을 끈 앱 목록 (notiFlag=false)


| Element     | Content                  |
| ----------- | ------------------------ |
| List item   | 동일 + trailing `제외 해제` 버튼 |
| Empty state | `제외 앱 리스트가 없습니다.`        |


---

### 4.6 Hole Tab (전체)

**Purpose**: DB에 등록된 **모든** 카메라/마이크 권한 앱 (필터 없음)


| Element       | Content                                  |
| ------------- | ---------------------------------------- |
| Same as Usage | but includes apps with notifications off |
| Empty state   | `목록 없음`                                  |


---

### 4.7 App Detail

**Purpose**: 단일 앱 상세 정보


| Element      | Content                             |
| ------------ | ----------------------------------- |
| Top bar      | `상세 페이지` + back                     |
| Header       | 64dp 앱 아이콘, 앱 이름, 패키지명              |
| Card: Camera | 카메라 권한 정보 — 허용 여부, 사용 횟수, 마지막 사용 일자 |
| Card: Audio  | 오디오 권한 정보 — 동일                      |
| Empty        | `표시할 데이터가 없습니다.`                    |


---

### 4.8 Settings

**Purpose**: 전역 설정


| Element     | Content                    |
| ----------- | -------------------------- |
| Top bar     | `설정` + back                |
| Row         | `전체 알림` + Switch           |
| Description | 카메라·마이크 사용 감지 알림을 켜거나 끕니다. |


**Future expansion ideas** (디자인 시 여백 확보):

- 권한 설정 바로가기
- 앱 정보 / 버전
- King 전체 알림 끄기 단축

---

### 4.9 Camera Intercept (풀스크린 오버레이)

**Purpose**: 타 앱 카메라 사용 **즉시 알림** — 가장 중요한 UX


| Element          | Content                                                                   |
| ---------------- | ------------------------------------------------------------------------- |
| Layout           | Full screen, subtle primary tint background                               |
| Preview area     | 220dp height, 24dp radius — **실시간 전면 카메라 프리뷰** (보안: King이 카메라 점유 중임을 시각화) |
| Title            | `카메라 사용이 감지되었습니다`                                                         |
| Body             | `{앱이름} 에서 카메라를 사용 중입니다. 의심스러운 사용이라면 즉시 앱을 확인해 주세요.`                       |
| Checkbox 1       | `현재 앱 알림 끄기`                                                              |
| Checkbox 2       | `오늘 하루 알림 끄기` (전체 알림 off)                                                 |
| Button Primary   | `확인`                                                                      |
| Button Secondary | `설정하기` (해당 앱 시스템 설정)                                                      |


**UX priority**: 긴급하지만 panic-free. 빨리 닫거나 조치할 수 있어야 함.

---

###  

---

## 5. Key UX Principles for This App

1. **감지 알림은 3초 안에 이해 가능**해야 함 (무슨 앱, 무슨 행위, 무엇을 할지)
2. **False alarm fatigue 방지** — 제외/알림 끄기가 2탭 이내
3. **다크 테마 기본** — 배터리·보안 앱 이미지, 야간 사용
4. **한국어 우선** — 모든 라벨 한글, 숫자·날짜는 한국 형식 (MM월 dd일)
5. **리스트 중심 UI** — 메인 3탭 모두 앱 리스트 패턴 공유
6. **시스템 권한과 조화** — Android 13+ notification, usage stats 설정 유도

---

## 6. Competitive Reference (Moodboard direction)

- Android **Digital Wellbeing** / **Privacy Dashboard** — 깔끔한 권한·사용 이력
- **GlassWire** / **Access Dots** — 실시간 감지 인디케이터
- **Samsung Permission Monitor** — 인터셉트 팝업 톤
- Material 3 **Expressive** 보다 **기능적·미니멀** 쪽

Avoid: 해커/매트릭스 클리셰, 과도한 빨간 경고, 복잡한 대시보드

---

*Generated from CameraAudioTracker codebase — King v1.0.0.0*