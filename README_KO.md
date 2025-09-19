# BubbleTea (버블티)

BubbleTea는 좋아하는 버블티 음료를 주문할 수 있는 Android 애플리케이션입니다.

## 프로젝트 구조

이 프로젝트는 다음과 같은 표준 Android 프로젝트 구조로 생성되었습니다:
- **패키지명**: `com.happyhope.bubbletea`
- **최소 SDK**: API 24 (Android 7.0)
- **타겟 SDK**: API 34 (Android 14)
- **빌드 도구**: Android Gradle Plugin 8.2.0
- **언어**: Kotlin
- **UI 프레임워크**: Jetpack Compose

## 기능

- 간단하고 직관적인 사용자 인터페이스
- Material Design 3 테마 적용
- 라이트 및 다크 테마 지원
- 네비게이션 시스템 구성

## 프로젝트 빌드

### 필수 요구사항

- Android Studio Arctic Fox 이상
- Java 8 이상
- Android SDK API level 34

### 빌드 명령어

```bash
# 디버그 APK 빌드
./gradlew assembleDebug

# 릴리즈 APK 빌드
./gradlew assembleRelease

# 테스트 실행
./gradlew test

# 연결된 기기에 설치
./gradlew installDebug
```

### Android Studio에서 열기

1. Android Studio 실행
2. "Open an Existing Project" 선택
3. 프로젝트 디렉토리로 이동
4. "OK" 클릭

## 프로젝트 구조

```
app/
├── src/
│   └── main/
│       ├── java/com/happyhope/bubbletea/
│       │   ├── MainActivity.kt
│       │   └── ui/theme/
│       │       ├── Color.kt
│       │       ├── Theme.kt
│       │       └── Type.kt
│       ├── res/
│       │   ├── layout/
│       │   ├── values/
│       │   │   ├── colors.xml
│       │   │   ├── strings.xml
│       │   │   └── themes.xml
│       │   ├── values-night/
│       │   │   └── themes.xml
│       │   └── mipmap-*/
│       │       └── ic_launcher.png
│       └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## 개발 환경 설정 완료

✅ **Android Studio 프로젝트 생성**: 완료  
✅ **GitHub 저장소 연동**: 완료  
✅ **기본 앱 스켈레톤**: 완료  
✅ **네비게이션 시스템**: 완료  
✅ **테마 설정**: 완료  

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.