# 홍길동 - 홍익대학교 도보 지도 앱

복잡한 홍익대학교 캠퍼스에서 시설을 쉽게 찾고, 길을 안내받을 수 있는 Android 도보 지도 애플리케이션입니다.

---

## 주요 기능

### 지도 & 길찾기
- **네이버 지도** 기반 캠퍼스 지도 표시
- 출발지 · 도착지 설정 후 **도보 경로 안내**
- 주변 시설 **마커 표시** (북마크, 검색 결과, 추천 시설)
- 카메라 애니메이션 및 트래킹 모드

### 시설 검색
- **자동완성** 검색 및 자유어 검색
- 시설 유형별 필터 검색 (카페, 식당, 공부 공간 등)
- 최근 검색어 저장 (로컬 DB)
- 검색 결과에서 시설 상세 정보로 바로 이동

### 시설 상세
- 시설 기본 정보 (이름, 위치, 운영 시간 등)
- **사진 갤러리** 및 전체화면 뷰어
- **리뷰 및 평점** 조회
- 건물별 층 정보 확인
- 이벤트 정보 및 위치 확인

### 추천
- 카테고리별 추천 시설 (휴식, 공부, 전망, 회의, 식사)
- 좋아요 · 북마크 기능
- 진행 중인 이벤트 모아보기

### 리뷰
- 리뷰 작성 / 수정 / 삭제 (이미지 첨부 포함)
- 리뷰 좋아요 기능
- 내가 작성한 리뷰 / 좋아요한 리뷰 목록 확인

### 북마크
- 폴더 단위로 시설 즐겨찾기 관리
- 폴더 생성 · 수정 · 삭제
- 지도에서 북마크 위치 표시

### 회원
- 이메일 회원가입 (약관 동의 → 이메일 → 비밀번호 → 프로필 정보)
- 로그인 / 로그아웃
- 프로필 정보 수정 (닉네임, 프로필 사진 등)

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Kotlin |
| **UI** | Jetpack Compose, Material 3 |
| **아키텍처** | MVVM + Repository Pattern |
| **DI** | Hilt 2.57 |
| **비동기** | Kotlin Coroutines, StateFlow |
| **네트워크** | Retrofit 2.9, OkHttp 4.12 |
| **JSON 직렬화** | Moshi 1.15 |
| **이미지 로딩** | Coil 2.6 |
| **지도** | Naver Maps SDK 3.21, naver-map-compose 1.7 |
| **위치** | Google Play Services Location 21 |
| **로컬 DB** | Room 2.7 |
| **파일 업로드** | AWS S3 (Presigned URL 방식) |
| **UI 효과** | Haze 0.5 (블러 효과) |
| **내비게이션** | Navigation Compose |
| **코드 생성** | KSP 2.0 |

---

## 아키텍처

```
presentation (UI)
    ├── Screen (Compose)
    └── ViewModel (StateFlow / Hilt)

domain
    └── Repository Interface

data
    ├── Repository Implementation
    ├── Remote (Retrofit API Services)
    └── Local (Room Database)
```

- **UiState** sealed interface로 Initial / Loading / Success / Error 상태 관리
- **DefaultResponse** sealed class로 API 응답을 Success / Error로 추상화
- ViewModelScope 기반 비동기 처리, Dispatchers.IO로 네트워크·DB 분리

---

## 프로젝트 구조

```
com.hongildong.map
├── data
│   ├── dao/            # Room DAO
│   ├── db/             # 데이터베이스 설정
│   ├── entity/         # 데이터 모델
│   ├── module/         # Hilt 모듈
│   ├── remote
│   │   ├── api/        # Retrofit 서비스 인터페이스
│   │   ├── request/    # 요청 모델
│   │   └── response/   # 응답 모델
│   ├── repository/     # Repository 인터페이스 & 구현체
│   └── util/           # API 응답 래퍼, 에러 처리
├── navGraph/           # Navigation 구조 정의
└── ui
    ├── home/           # 홈 화면 (지도, 추천)
    ├── search/         # 검색 & 시설 상세
    ├── bookmark/       # 북마크 관리
    ├── profile/        # 프로필
    ├── user/           # 인증 (로그인, 회원가입)
    ├── theme/          # 앱 테마
    └── util/           # 공통 UI 컴포넌트
```

---

## 화면 구성

```
앱 진입
 ├── 로그인
 └── 회원가입 (약관 → 이메일 → 비밀번호 → 정보 입력)

메인 (하단 탭)
 ├── 홈 (지도 + 추천 시설)
 ├── 검색
 │    ├── 검색 결과 목록
 │    ├── 시설 상세 (정보 / 사진 / 리뷰)
 │    ├── 건물 상세 (층 정보)
 │    ├── 이벤트 상세
 │    └── 길찾기
 ├── 북마크 (폴더 목록 → 폴더 내 시설)
 └── 프로필 (내 리뷰 / 좋아요)
```

---

## 설정

`AndroidManifest.xml`에 **네이버 지도 API 키** 등록이 필요합니다.

```xml
<meta-data
    android:name="com.naver.maps.map.CLIENT_ID"
    android:value="YOUR_NAVER_MAP_API_KEY" />
```

필요 권한:
- `INTERNET`
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`

---

## 개발 환경

- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 36
- **Java 호환성:** Java 11
- **빌드 도구:** Gradle Kotlin DSL
