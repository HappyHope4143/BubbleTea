# NewsAPI 연동 가이드

## 개요
이 애플리케이션은 [NewsAPI.org](https://newsapi.org/)를 연동하여 실시간 IT 및 과학 관련 뉴스를 제공합니다.

## 구현 내용

### 1. Retrofit 네트워크 레이어 구현 ✅

#### API 엔드포인트
- **사용 엔드포인트**: `/v2/top-headlines`
- **카테고리**: `technology` (IT/과학 기술 뉴스)
- **국가**: `us` (미국)
- **페이지 크기**: 20개 기사

#### API 키 보안 관리
```gradle
// app/build.gradle
buildConfigField "String", "NEWS_API_KEY", "\"${localProperties.getProperty('newsapi.apiKey', 'demo-api-key')}\""
```

- ✅ `local.properties`에서 API 키 읽기
- ✅ `BuildConfig.NEWS_API_KEY`로 컴파일 타임 주입
- ✅ 소스 코드에 직접 노출하지 않음
- ✅ `.gitignore`에 `local.properties` 추가됨

#### 데이터 모델 (DTO)
```kotlin
// NewsApiModel.kt
data class NewsResponse(
    val articles: List<NewsApiModel>,
    val status: String,
    val totalResults: Int
)

data class NewsApiModel(
    val title: String?,
    val description: String?,
    val url: String?,
    val publishedAt: String?,
    val urlToImage: String?,
    val source: NewsSourceModel?
)
```

파싱 내용:
- ✅ 기사 제목 (title)
- ✅ 요약 (description)
- ✅ URL (url)
- ✅ 출처 (source.name)
- ✅ 발행 시간 (publishedAt)

### 2. Repository/데이터 레이어 통합 ✅

#### 네트워크 데이터 저장
```kotlin
// NewsRepositoryImpl.kt
override suspend fun refreshNews(): Result<Unit> {
    val response = newsApiService.getTopHeadlines(
        category = "technology",
        country = "us",
        pageSize = 20,
        apiKey = BuildConfig.NEWS_API_KEY
    )
    
    if (response.status == "ok" && response.articles.isNotEmpty()) {
        val newsEntities = mapper.mapApiListToEntityList(response.articles)
        newsDao.insertNews(newsEntities)
        clearOldNews()
        return Result.success(Unit)
    }
}
```

#### Room DB 저장 정책
- ✅ 네트워크에서 받은 뉴스 데이터를 Room DB에 저장
- ✅ 최신 N개(100개) 유지 정책 반영
- ✅ `clearOldNews()` 함수로 오래된 데이터 자동 삭제

```kotlin
override suspend fun clearOldNews() {
    val currentCount = newsDao.getNewsCount()
    if (currentCount > BubbleTeaDatabase.NEWS_LIMIT) {  // 100
        val excessCount = currentCount - BubbleTeaDatabase.NEWS_LIMIT
        newsDao.deleteOldestNews(excessCount)
    }
}
```

#### 네트워크 실패 시 캐시 활용
```kotlin
override suspend fun getNews(): Flow<List<News>> {
    return flow {
        // 1. 먼저 캐시된 데이터 emit
        val localNews = getLocalNews()
        if (localNews.isNotEmpty()) {
            emit(localNews)
        }
        
        // 2. 네트워크에서 새로운 데이터 가져오기 시도
        val refreshResult = refreshNews()
        if (refreshResult.isSuccess) {
            // 성공 시 업데이트된 데이터 emit
            val updatedNews = getLocalNews()
            if (updatedNews.isNotEmpty()) {
                emit(updatedNews)
            }
        } else if (localNews.isEmpty()) {
            // 캐시도 없고 네트워크도 실패한 경우에만 샘플 데이터 사용
            val sampleNews = sampleNewsProvider.getSampleNews()
            newsDao.insertNews(sampleNews)
            emit(mapper.mapEntitiesToDomain(sampleNews))
        }
    }
}
```

데이터 흐름:
1. **캐시 우선 표시**: 로컬 DB에 데이터가 있으면 먼저 표시
2. **백그라운드 갱신**: 네트워크에서 최신 데이터 가져오기
3. **정상 흐름**: 새 데이터 받아서 DB 업데이트 → 화면 갱신
4. **실패 흐름**: 캐시된 데이터 계속 표시 (사용자에게 투명)
5. **최악 시나리오**: 캐시도 없고 네트워크도 실패 시 샘플 데이터 표시

### 3. 보안 및 약관 준수 ✅

#### API 키 보안
✅ **구현된 보안 조치**:
- `local.properties`에 API 키 저장 (Git에 커밋되지 않음)
- `BuildConfig`를 통한 컴파일 타임 주입
- 소스 코드에 하드코딩하지 않음
- `local.properties.template` 제공으로 개발자 가이드

#### 뉴스 출처 표기
✅ **구현된 UI 반영**:
- 각 뉴스 항목에 출처(source) 표시
- 원문 URL 링크 제공
- 발행 시간 표시

```kotlin
// NewsEntity.kt
data class NewsEntity(
    val title: String,
    val summary: String,
    val url: String,
    val source: String,  // 출처 표기
    val createdAt: Long  // 시간 표기
)
```

## API 사용량 및 제한

### NewsAPI 무료 플랜
- **일일 요청 한도**: 100회
- **월간 요청 한도**: 1000회
- **요청당 결과**: 최대 100개 기사

### 에러 처리
앱은 다음 상황을 자동으로 처리합니다:

1. **API 키 오류**
   ```kotlin
   catch (e: Exception) {
       println("NewsAPI Error: ${e.message}")
       Result.failure(e)
   }
   ```

2. **사용량 초과**
   - 캐시된 데이터 계속 표시
   - 사용자에게 별도 에러 메시지 없음
   - 다음 갱신 시 재시도

3. **네트워크 오류**
   - 오프라인 상태에서도 캐시 데이터 표시
   - 네트워크 복구 시 자동 갱신

## 설정 방법

### 1. API 키 발급
1. [NewsAPI.org](https://newsapi.org/register) 접속
2. 무료 계정 생성
3. API 키 복사

### 2. API 키 설정
```bash
# 템플릿 복사
cp local.properties.template local.properties

# local.properties 편집
vi local.properties
```

```properties
# local.properties 내용
# Android SDK 경로 (예시)
# Linux/Mac: sdk.dir=/home/username/Android/Sdk 또는 ~/Android/Sdk
# Windows: sdk.dir=C:\\Users\\username\\AppData\\Local\\Android\\Sdk
sdk.dir=/usr/local/lib/android/sdk

# NewsAPI 키
newsapi.apiKey=YOUR_API_KEY_HERE
```

### 3. 빌드 및 실행
```bash
# 클린 빌드
./gradlew clean

# 디버그 빌드
./gradlew assembleDebug

# 설치
./gradlew installDebug
```

## 커스터마이징

### 뉴스 카테고리 변경
`NewsRepositoryImpl.kt` 파일에서 카테고리 변경 가능:

```kotlin
val response = newsApiService.getTopHeadlines(
    category = "technology", // 변경 가능: business, science, health, sports 등
    country = "us",
    pageSize = 20,
    apiKey = BuildConfig.NEWS_API_KEY
)
```

지원 카테고리:
- `business` - 비즈니스
- `technology` - 기술 (현재 설정)
- `science` - 과학
- `health` - 건강
- `sports` - 스포츠
- `entertainment` - 엔터테인먼트

### 국가 변경
```kotlin
country = "us", // 변경 가능: kr, jp, gb, de, fr 등
```

한국 뉴스를 받으려면 `country = "kr"`로 변경

### 캐시 크기 조정
`BubbleTeaDatabase.kt`에서 제한 변경:

```kotlin
companion object {
    const val NEWS_LIMIT = 100 // 원하는 개수로 변경
}
```

## 구조 및 파일

### 주요 파일
```
app/src/main/java/com/happyhope/bubbletea/
├── data/
│   ├── api/
│   │   ├── model/NewsApiModel.kt          # API 응답 모델
│   │   └── service/NewsApiService.kt      # Retrofit 인터페이스
│   ├── dao/NewsDao.kt                     # Room DAO
│   ├── entity/NewsEntity.kt               # Room 엔티티
│   ├── mapper/NewsDataMapper.kt           # 데이터 변환
│   ├── repository/NewsRepositoryImpl.kt   # 저장소 구현
│   └── local/SampleNewsProvider.kt        # 샘플 데이터 (폴백용)
├── domain/
│   ├── model/News.kt                      # 도메인 모델
│   ├── repository/NewsRepository.kt       # 저장소 인터페이스
│   └── usecase/
│       ├── GetNewsUseCase.kt
│       └── RefreshNewsUseCase.kt
└── di/
    └── NetworkModule.kt                   # Retrofit/OkHttp 설정
```

### 설정 파일
```
BubbleTea/
├── local.properties                       # API 키 (Git 제외)
├── local.properties.template              # 설정 템플릿
├── NEWS_API_INTEGRATION.md               # 영문 가이드
└── app/build.gradle                      # BuildConfig 설정
```

## 테스트

### 단위 테스트 실행
```bash
./gradlew testDebugUnitTest
```

테스트 범위:
- ✅ 저장소 캐싱 로직
- ✅ 데이터 매핑
- ✅ 오래된 뉴스 정리
- ✅ 에러 처리

### 수동 테스트 체크리스트
- [ ] 앱 최초 실행 시 실제 뉴스 로드
- [ ] Pull-to-refresh로 뉴스 갱신
- [ ] 비행기 모드에서 캐시된 뉴스 표시
- [ ] API 키 오류 시 적절한 메시지 표시
- [ ] 사용량 초과 시 캐시 데이터 표시

## 규정 준수

### NewsAPI 이용 약관
⚠️ **중요**: [NewsAPI 약관](https://newsapi.org/terms) 반드시 확인

주요 요구사항:
- ✅ 뉴스 출처 표기 (구현됨)
- ✅ 원문 링크 제공 (구현됨)
- ⚠️ 상업적 사용은 유료 플랜 필요
- ⚠️ 콘텐츠 재배포는 허가 없이 불가

### 저작권
앱에서 표시하는 내용:
- ✅ 기사 출처명
- ✅ 원문 링크
- ✅ 발행 날짜

이는 NewsAPI의 attribution 요구사항을 준수합니다.

## 문제 해결

### "API Key Invalid" 오류
- `local.properties`에 올바른 API 키 확인
- 불필요한 공백이나 따옴표 제거
- 프로젝트 재빌드: `./gradlew clean assembleDebug`

### 뉴스가 표시되지 않음
- 인터넷 연결 확인
- NewsAPI 대시보드에서 API 키 활성화 확인
- 일일/월간 사용량 제한 확인
- 로그에서 API 에러 메시지 확인

### 빌드 오류
- `local.properties` 파일 존재 확인
- `./gradlew clean` 실행
- Android Studio에서 Gradle 파일 동기화
- Android SDK 설치 확인

## 향후 개선 사항
- [ ] 검색 기능 추가
- [ ] 여러 뉴스 카테고리 지원
- [ ] 페이지네이션으로 더 많은 기사 로드
- [ ] 기사 썸네일 이미지 로딩
- [ ] Rate limiting을 위한 exponential backoff 구현
- [ ] 카테고리/출처 사용자 설정
- [ ] 다국어 지원

## 참고 자료
- [NewsAPI 문서](https://newsapi.org/docs)
- [Retrofit 문서](https://square.github.io/retrofit/)
- [Room 데이터베이스 가이드](https://developer.android.com/training/data-storage/room)
- [Android BuildConfig](https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code)
