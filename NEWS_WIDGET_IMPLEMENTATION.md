# News Widget Implementation (뉴스 위젯 구현)

## Overview (개요)

This document describes the News Widget implementation using Compose Glance for the BubbleTea application.

이 문서는 BubbleTea 애플리케이션의 Compose Glance를 사용한 뉴스 위젯 구현에 대해 설명합니다.

## Features (기능)

### Implemented Features (구현된 기능)

1. **Latest News Display** - Shows up to 3 latest news headlines with summaries
   - 최신 뉴스 표시 - 최대 3개의 최신 뉴스 헤드라인과 요약 표시

2. **News Item Click** - Tapping on a news item opens the main app
   - 뉴스 항목 클릭 - 뉴스 항목을 탭하면 메인 앱이 열림

3. **Refresh Button** - Manual refresh button to update news data
   - 새로고침 버튼 - 뉴스 데이터를 업데이트하는 수동 새로고침 버튼

4. **Data Integration** - Uses the same data source as the home screen (Room database)
   - 데이터 연동 - 홈 화면과 동일한 데이터 소스 사용 (Room 데이터베이스)

## Architecture (아키텍처)

### Components (구성요소)

#### 1. NewsWidget.kt
- Main widget class extending `GlanceAppWidget`
- Defines the widget UI using Compose Glance
- Displays news headlines, summaries, source, and timestamp
- 메인 위젯 클래스, `GlanceAppWidget` 확장
- Compose Glance를 사용하여 위젯 UI 정의
- 뉴스 헤드라인, 요약, 출처 및 타임스탬프 표시

#### 2. NewsWidgetReceiver.kt
- Broadcast receiver for widget updates
- Handles system widget lifecycle events
- 위젯 업데이트를 위한 브로드캐스트 리시버
- 시스템 위젯 수명 주기 이벤트 처리

#### 3. NewsWidgetRepository.kt
- Singleton repository for widget data access
- Accesses Room database directly for widget
- Provides cached news data
- 위젯 데이터 액세스를 위한 싱글톤 레포지토리
- 위젯을 위해 Room 데이터베이스에 직접 액세스
- 캐시된 뉴스 데이터 제공

#### 4. RefreshNewsAction.kt
- Action callback for refresh button
- Updates news data from sample provider
- Refreshes all widget instances
- 새로고침 버튼을 위한 액션 콜백
- 샘플 프로바이더에서 뉴스 데이터 업데이트
- 모든 위젯 인스턴스 새로고침

### Data Flow (데이터 흐름)

```
User Interaction → RefreshNewsAction → Database Update → Widget Refresh
사용자 상호작용 → RefreshNewsAction → 데이터베이스 업데이트 → 위젯 새로고침

Widget Display → NewsWidgetRepository → Room Database → News Entities
위젯 표시 → NewsWidgetRepository → Room 데이터베이스 → 뉴스 엔티티
```

## Dependencies (의존성)

Added to `app/build.gradle`:

```gradle
// Glance for App Widgets
implementation "androidx.glance:glance-appwidget:1.0.0"
implementation "androidx.glance:glance-material3:1.0.0"
```

## Configuration Files (설정 파일)

### 1. AndroidManifest.xml

```xml
<receiver
    android:name=".widget.NewsWidgetReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/news_widget_info" />
</receiver>
```

### 2. res/xml/news_widget_info.xml
- Widget metadata configuration
- Defines size, update period, and preview
- 위젯 메타데이터 구성
- 크기, 업데이트 주기 및 미리보기 정의

### 3. res/layout/news_widget_loading.xml
- Initial loading layout shown while widget initializes
- 위젯 초기화 중 표시되는 초기 로딩 레이아웃

## Usage (사용법)

### Adding Widget to Home Screen (홈 화면에 위젯 추가)

1. Long press on empty space on home screen
   - 홈 화면의 빈 공간을 길게 누르기
   
2. Select "Widgets" from menu
   - 메뉴에서 "위젯" 선택
   
3. Find "BubbleTea" app and select "Latest News" widget
   - "BubbleTea" 앱을 찾아 "Latest News" 위젯 선택
   
4. Drag and place widget on home screen
   - 위젯을 드래그하여 홈 화면에 배치

### Widget Features (위젯 기능)

- **Automatic Updates**: Widget refreshes every 30 minutes automatically
  - 자동 업데이트: 위젯이 30분마다 자동으로 새로고침
  
- **Manual Refresh**: Tap the refresh icon to update immediately
  - 수동 새로고침: 새로고침 아이콘을 탭하여 즉시 업데이트
  
- **News Navigation**: Tap any news item to open the app
  - 뉴스 탐색: 뉴스 항목을 탭하여 앱 열기

## Widget UI Components (위젯 UI 구성요소)

### Header (헤더)
- Title: "Latest News"
- Refresh icon button
- 제목: "Latest News"
- 새로고침 아이콘 버튼

### News Items (뉴스 항목)
Each news item shows:
- **Title**: News headline (max 2 lines)
- **Summary**: Brief description (max 2 lines)
- **Source**: News source name
- **Timestamp**: Publication time

각 뉴스 항목 표시:
- **제목**: 뉴스 헤드라인 (최대 2줄)
- **요약**: 간단한 설명 (최대 2줄)
- **출처**: 뉴스 출처 이름
- **타임스탬프**: 게시 시간

## Technical Details (기술 세부사항)

### Widget Size (위젯 크기)
- Minimum width: 250dp
- Minimum height: 180dp
- Resizable: Both horizontal and vertical
- 최소 너비: 250dp
- 최소 높이: 180dp
- 크기 조정 가능: 가로 및 세로

### Update Frequency (업데이트 빈도)
- Automatic: Every 30 minutes (1800000ms) - subject to Android system limits and battery optimization
- Manual: User-triggered via refresh button
- 자동: 30분마다 (1800000ms) - Android 시스템 제한 및 배터리 최적화에 따라 달라질 수 있음
- 수동: 새로고침 버튼을 통한 사용자 트리거

**Note**: Actual update frequency may be extended by Android's widget update limits and device battery optimization settings.

**참고**: 실제 업데이트 빈도는 Android의 위젯 업데이트 제한 및 기기 배터리 최적화 설정에 따라 연장될 수 있습니다.

### Performance Considerations (성능 고려사항)

1. **Database Access**: Uses singleton pattern to avoid multiple database instances
   - 데이터베이스 액세스: 싱글톤 패턴을 사용하여 여러 데이터베이스 인스턴스 방지

2. **Background Operations**: All database operations run on IO dispatcher
   - 백그라운드 작업: 모든 데이터베이스 작업이 IO 디스패처에서 실행

3. **Error Handling**: Gracefully handles errors by showing cached data
   - 오류 처리: 캐시된 데이터를 표시하여 오류를 우아하게 처리

## Future Enhancements (향후 개선사항)

Potential improvements:
- [ ] Add widget configuration activity for customization
- [ ] Support different widget sizes with varying content
- [ ] Add loading and error states
- [ ] Implement deep linking to specific news article
- [ ] Add animation transitions
- [ ] Add data encryption for cached news content
- [ ] Implement secure API communication validation
- [ ] Add user preferences for news categories

잠재적 개선사항:
- [ ] 사용자 지정을 위한 위젯 구성 액티비티 추가
- [ ] 다양한 콘텐츠를 가진 다양한 위젯 크기 지원
- [ ] 로딩 및 오류 상태 추가
- [ ] 특정 뉴스 기사로의 딥 링킹 구현
- [ ] 애니메이션 전환 추가
- [ ] 캐시된 뉴스 콘텐츠에 대한 데이터 암호화 추가
- [ ] 보안 API 통신 검증 구현
- [ ] 뉴스 카테고리에 대한 사용자 기본 설정 추가

## Testing (테스트)

### Manual Testing Steps (수동 테스트 단계)

1. Build and install the app
   - 앱 빌드 및 설치
   
2. Add widget to home screen
   - 홈 화면에 위젯 추가
   
3. Verify news items display correctly
   - 뉴스 항목이 올바르게 표시되는지 확인
   
4. Test refresh button functionality
   - 새로고침 버튼 기능 테스트
   
5. Test news item click navigation
   - 뉴스 항목 클릭 탐색 테스트
   
6. Verify automatic updates after 30 minutes
   - 30분 후 자동 업데이트 확인

### Build Verification (빌드 검증)

All builds pass successfully:
- ✅ Debug build: `./gradlew assembleDebug`
- ✅ Release build: `./gradlew assembleRelease`
- ✅ Unit tests: `./gradlew testDebugUnitTest`
- ✅ Lint checks: `./gradlew lintDebug`

## Files Modified/Created (수정/생성된 파일)

### Modified (수정됨)
- `app/build.gradle` - Added Glance dependencies
- `app/src/main/AndroidManifest.xml` - Registered widget receiver
- `app/src/main/res/values/strings.xml` - Added widget strings

### Created (생성됨)
- `app/src/main/java/com/happyhope/bubbletea/widget/NewsWidget.kt`
- `app/src/main/java/com/happyhope/bubbletea/widget/NewsWidgetReceiver.kt`
- `app/src/main/java/com/happyhope/bubbletea/widget/NewsWidgetRepository.kt`
- `app/src/main/java/com/happyhope/bubbletea/widget/RefreshNewsAction.kt`
- `app/src/main/res/drawable/ic_refresh.xml`
- `app/src/main/res/layout/news_widget_loading.xml`
- `app/src/main/res/xml/news_widget_info.xml`

## References (참조)

**Note**: Documentation URLs are current as of implementation date. Please verify these links are still active.

**참고**: 문서 URL은 구현 날짜 기준입니다. 이러한 링크가 여전히 활성화되어 있는지 확인하십시오.

- [Compose Glance Documentation](https://developer.android.com/jetpack/compose/glance)
- [App Widgets Overview](https://developer.android.com/develop/ui/views/appwidgets/overview)
- [Glance App Widget Codelab](https://developer.android.com/codelabs/jetpack-compose-for-app-widgets)
