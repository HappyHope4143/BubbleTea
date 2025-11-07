# AdMob 통합 가이드

## 개요
BubbleTea 앱에 Google AdMob 배너 광고가 통합되었습니다. 현재 테스트 광고 ID를 사용하고 있으며, 프로덕션 배포 시 실제 광고 ID로 전환이 필요합니다.

## 현재 설정

### AdMob App ID
- **위치**: `app/src/main/AndroidManifest.xml`
- **현재 값**: `ca-app-pub-7298112669115589~5298316916`
- **상태**: ✅ 프로덕션 ID 설정 완료

### 광고 단위 ID

#### 테스트 광고 ID (현재 사용 중)
- **위치**: `app/src/main/java/com/happyhope/bubbletea/presentation/ads/AdMobBanner.kt`
- **현재 값**: `ca-app-pub-3940256099942544/6300978111`
- **용도**: 개발 및 테스트 단계

#### 프로덕션 광고 ID
- **발급된 ID**: `ca-app-pub-7298112669115589/6162351181`
- **용도**: 실제 앱 배포 및 수익화

## 프로덕션 광고 ID로 전환하기

### 방법 1: 코드에서 직접 변경 (간단)

`AdMobBanner.kt` 파일의 기본값을 변경합니다:

```kotlin
// 파일: app/src/main/java/com/happyhope/bubbletea/presentation/ads/AdMobBanner.kt

@Composable
fun AdMobBanner(
    adUnitId: String = "ca-app-pub-7298112669115589/6162351181", // 프로덕션 광고 ID
    modifier: Modifier = Modifier
) {
    // ... 나머지 코드
}
```

### 방법 2: BuildConfig를 통한 환경별 관리 (권장)

더 나은 방법은 debug/release 빌드 타입에 따라 자동으로 광고 ID를 전환하는 것입니다.

#### 1단계: build.gradle 수정

```gradle
// 파일: app/build.gradle

android {
    defaultConfig {
        // ... 기존 설정
        
        // AdMob Ad Unit ID 주입
        buildConfigField "String", "ADMOB_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/6300978111\""
    }
    
    buildTypes {
        release {
            // ... 기존 설정
            
            // 프로덕션 광고 ID 사용
            buildConfigField "String", "ADMOB_AD_UNIT_ID", "\"ca-app-pub-7298112669115589/6162351181\""
        }
        debug {
            // 테스트 광고 ID 사용 (defaultConfig에서 상속)
        }
    }
}
```

#### 2단계: AdMobBanner.kt 수정

```kotlin
// 파일: app/src/main/java/com/happyhope/bubbletea/presentation/ads/AdMobBanner.kt

import com.happyhope.bubbletea.BuildConfig

@Composable
fun AdMobBanner(
    adUnitId: String = BuildConfig.ADMOB_AD_UNIT_ID, // BuildConfig에서 자동 선택
    modifier: Modifier = Modifier
) {
    // ... 나머지 코드
}
```

이렇게 하면:
- **Debug 빌드**: 자동으로 테스트 광고 표시
- **Release 빌드**: 자동으로 프로덕션 광고 표시

## 광고 배치 위치

### NewsScreen (뉴스 리스트 화면)
- **위치**: 화면 하단에 배너 광고
- **파일**: `app/src/main/java/com/happyhope/bubbletea/presentation/news/NewsScreen.kt`
- **크기**: AdSize.BANNER (320x50dp)

## 테스트 방법

### 테스트 광고 확인
1. 앱을 빌드하고 실행
2. 뉴스 화면으로 이동
3. 화면 하단에 "Test Ad" 라벨이 있는 광고 배너가 표시되어야 함

### 프로덕션 광고 테스트 (출시 전)
1. 광고 ID를 프로덕션 ID로 변경
2. Release 빌드 생성: `./gradlew assembleRelease`
3. 테스트 기기에 설치하여 실제 광고가 표시되는지 확인
4. ⚠️ **주의**: 본인의 광고를 반복적으로 클릭하지 마세요 (정책 위반)

## AdMob 정책 준수 사항

### 필수 준수 사항
1. **자신의 광고 클릭 금지**: 개발자가 본인의 광고를 클릭하면 계정이 정지될 수 있습니다
2. **테스트 기기 등록**: AdMob 콘솔에서 테스트 기기를 등록하여 안전하게 테스트
3. **사용자 동의**: 개인정보 보호 정책에 광고 사용 명시
4. **부적절한 콘텐츠 금지**: 광고와 함께 표시되는 콘텐츠 관리

### 권장 사항
- 광고 로딩 실패 처리
- 사용자 경험을 해치지 않는 광고 배치
- 광고 새로고침 빈도 조절

## 문제 해결

### 광고가 표시되지 않는 경우

1. **인터넷 연결 확인**
   - AndroidManifest.xml에 INTERNET 권한이 있는지 확인
   - 실제 인터넷 연결 상태 확인

2. **AdMob 계정 상태 확인**
   - AdMob 콘솔에서 앱이 승인되었는지 확인
   - 광고 단위가 활성화되어 있는지 확인

3. **테스트 광고 ID 사용**
   - 개발 중에는 항상 테스트 광고 ID 사용
   - 테스트 광고는 즉시 표시되어야 함

4. **로그 확인**
   ```bash
   adb logcat | grep -i "ads"
   ```

### 빌드 오류

**Kotlin 버전 호환성**
- 현재 AdMob SDK 버전: 23.3.0
- Kotlin 버전: 1.9.22
- 더 높은 AdMob 버전(24.x)을 사용하려면 Kotlin 2.1.0+ 필요

## 추가 리소스

- [Google AdMob 공식 문서](https://developers.google.com/admob/android)
- [AdMob 정책 가이드](https://support.google.com/admob/answer/6128543)
- [Jetpack Compose에서 AdMob 사용하기](https://developers.google.com/admob/android/banner)

## 변경 이력

- **2025-11-07**: 초기 AdMob 통합 완료
  - AdMob SDK 23.3.0 추가
  - NewsScreen에 배너 광고 추가
  - 테스트 광고 ID로 설정
