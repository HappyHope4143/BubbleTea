# BubbleTea 앱 출시 준비 체크리스트

이 문서는 BubbleTea Android 앱을 Google Play Store에 출시하기 위해 필요한 모든 작업들을 정리한 것입니다.

## 📋 목차
- [당신이 해야 할 작업](#당신이-해야-할-작업)
- [AI가 도와줄 수 있는 작업](#ai가-도와줄-수-있는-작업)
- [출시 후 필요한 작업](#출시-후-필요한-작업)

---

## 🔴 당신이 해야 할 작업

이 작업들은 사업적 결정, 법적 책임, 또는 외부 계정 생성이 필요하여 반드시 직접 수행해야 합니다.

### 1. Google Play Console 계정 설정 (필수)
**우선순위: 최고**

- [ ] Google Play Console 개발자 계정 생성
  - 비용: $25 (일회성 등록 수수료)
  - 소요 시간: 24-48시간 (계정 승인 대기)
  - 링크: https://play.google.com/console/signup
  - 필요 정보: Gmail 계정, 결제 수단, 신원 확인

- [ ] 개발자 프로필 완성
  - 개발자/회사 이름
  - 연락처 이메일 (공개됨)
  - 웹사이트 URL (선택사항이지만 권장)
  - 개인정보처리방침 URL (필수)

### 2. 법적 문서 및 정책 준비 (필수)
**우선순위: 최고**

- [ ] **개인정보처리방침 (Privacy Policy) 작성**
  - 필수: 앱이 수집하는 데이터 명시
  - 현재 앱이 수집하는 것:
    - 인터넷 접속 권한 (뉴스 API 사용)
    - AdMob 광고 데이터
    - NewsAPI.org를 통한 뉴스 데이터
  - 호스팅 필요: 공개 URL에서 접근 가능해야 함
  - 무료 호스팅 옵션: GitHub Pages, Firebase Hosting, Netlify

- [ ] **이용약관 (Terms of Service) 작성** (권장)
  - 앱 사용 조건 명시
  - 책임 제한 조항
  - 서비스 중단 가능성

- [ ] **저작권 및 라이선스 확인**
  - 사용 중인 오픈소스 라이브러리 라이선스 확인 (이미 MIT 라이선스 사용 중)
  - 앱 아이콘 및 이미지 저작권 확인

### 3. 앱 콘텐츠 및 마케팅 자료 준비 (필수)
**우선순위: 높음**

- [ ] **앱 스토어 등록 정보**
  - [ ] 앱 이름 (현재: "BubbleTea")
  - [ ] 짧은 설명 (80자 이하)
  - [ ] 상세 설명 (4,000자 이하)
    - 주요 기능 설명
    - 버블티 주문 기능 (현재는 뉴스 기능만 구현됨)
    - 기술 뉴스 제공 기능
  - [ ] 카테고리 선택
    - 추천: "음식 및 음료" 또는 "라이프스타일"

- [ ] **앱 스크린샷** (필수)
  - 최소 2개, 권장 4-8개
  - 크기: 16:9 또는 9:16 비율
  - 내용: 주요 화면 캡처
    - 메인 화면
    - 뉴스 화면
    - 위젯 화면
  - 다양한 화면 크기 지원 (폰, 태블릿)

- [ ] **앱 아이콘 (Feature Graphic)** (필수)
  - 크기: 1024 x 500 픽셀
  - PNG 형식
  - Play Store 상단에 표시되는 배너

- [ ] **홍보용 비디오** (선택사항, 권장)
  - YouTube 링크
  - 30초-2분 길이
  - 앱 주요 기능 시연

### 4. 앱 서명 키 (Signing Key) 생성 (필수)
**우선순위: 최고**

- [ ] **Release Keystore 생성**
  - 중요: 이 키를 잃어버리면 앱 업데이트 불가능!
  - 안전한 곳에 백업 필수 (클라우드 + 로컬)
  - 키 정보를 안전하게 보관:
    - Keystore 파일
    - Keystore 비밀번호
    - Key alias
    - Key 비밀번호

- [ ] **Play App Signing 설정**
  - Google이 키를 관리하는 옵션 (권장)
  - 키 분실 위험 감소

### 5. 앱 테스팅 (필수)
**우선순위: 높음**

- [ ] **실제 기기에서 테스트**
  - 최소 2-3가지 다른 기기에서 테스트
  - 다양한 Android 버전에서 테스트 (API 24-34)
  - 화면 크기별 테스트 (소형, 중형, 대형)

- [ ] **기능 테스트**
  - [ ] 앱 시작 및 종료
  - [ ] 뉴스 로딩 및 표시
  - [ ] 뉴스 클릭 시 브라우저 열기
  - [ ] 위젯 설치 및 작동
  - [ ] 다크 모드 전환
  - [ ] 네트워크 오류 처리
  - [ ] AdMob 광고 표시

- [ ] **성능 테스트**
  - 앱 로딩 시간
  - 메모리 사용량
  - 배터리 소모량
  - 네트워크 데이터 사용량

### 6. API 키 및 설정 (필수)
**우선순위: 최고**

- [ ] **NewsAPI.org API 키**
  - 현재: 무료 플랜 사용 중
  - 무료 플랜 제한:
    - 하루 100 요청
    - 개발 용도로만 사용 가능
  - ⚠️ **프로덕션 출시 시 유료 플랜 필요**
    - Business 플랜: $449/월
    - 또는 대체 뉴스 API 검토 필요

- [ ] **AdMob 설정**
  - 이미 설정됨: `ca-app-pub-7298112669115589~5298316916`
  - [ ] 광고 단위 ID 확인
  - [ ] 테스트 광고에서 실제 광고로 전환 확인
  - [ ] AdMob 계정에서 앱 등록 확인

### 7. 앱 콘텐츠 등급 (Content Rating) (필수)
**우선순위: 높음**

- [ ] Google Play Console에서 콘텐츠 등급 설문 작성
  - 폭력, 성적 콘텐츠, 약물, 도박 등 관련 질문
  - 현재 앱: 뉴스 콘텐츠만 제공하므로 "전체 이용가" 예상
  - 소요 시간: 약 10-15분

### 8. 데이터 보안 섹션 작성 (필수)
**우선순위: 최고**

- [ ] 앱이 수집하는 데이터 유형 명시
  - [ ] 위치 정보: 수집 안 함
  - [ ] 개인 정보: 수집 안 함
  - [ ] 앱 활동: AdMob 광고 추적
  - [ ] 디바이스 정보: 최소한의 정보

- [ ] 데이터 처리 방식
  - [ ] 암호화 전송 여부
  - [ ] 삭제 요청 처리 방법
  - [ ] 데이터 공유 대상 (AdMob, NewsAPI)

### 9. 대상 고객 및 콘텐츠 (필수)
**우선순위: 높음**

- [ ] 대상 연령 선택
  - 추천: "만 18세 이상" (뉴스 콘텐츠 포함)
  
- [ ] 가족 정책 준수 여부
  - 현재 앱: 광고 포함, 가족용 아님

### 10. 비즈니스 관련 결정 (선택사항)
**우선순위: 중간**

- [ ] **수익화 전략 결정**
  - 현재: AdMob 광고만 사용
  - 추가 옵션:
    - 프리미엄 버전 (광고 제거)
    - 인앱 구매
    - 구독 모델

- [ ] **출시 전략**
  - [ ] 전체 출시 vs 단계적 출시
  - [ ] 출시 국가 선택 (한국만 vs 글로벌)
  - [ ] 가격 정책 (무료 vs 유료)

---

## 🟢 AI가 도와줄 수 있는 작업

이 작업들은 기술적인 구현이 필요하며, AI가 자동으로 처리하거나 코드를 생성해줄 수 있습니다.

### 1. 앱 서명 설정 자동화
**현재 상태: 미완료** | **작업 시간: 10분**

- [ ] `app/build.gradle`에 서명 설정 추가
  - `signingConfigs` 블록 추가
  - Release 빌드에 서명 적용
  - keystore.properties 파일 사용하도록 설정

**AI가 할 일:**
```kotlin
// build.gradle에 자동으로 추가
signingConfigs {
    release {
        // keystore.properties에서 정보 읽어오기
    }
}
```

### 2. ProGuard/R8 최적화 설정
**현재 상태: 기본 설정만 있음** | **작업 시간: 20분**

- [ ] ProGuard 규칙 강화
  - 코드 난독화 활성화
  - 사용하지 않는 코드 제거
  - APK 크기 최적화
  - 라이브러리별 ProGuard 규칙 추가

**AI가 할 일:**
```kotlin
buildTypes {
    release {
        minifyEnabled true      // 현재 false → true로 변경
        shrinkResources true    // 사용하지 않는 리소스 제거
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 3. 버전 관리 자동화
**현재 상태: 수동 관리** | **작업 시간: 15분**

- [ ] 버전 코드 및 버전 이름 자동 증가
- [ ] Git 태그와 연동
- [ ] 빌드 번호 자동 생성

**현재 설정:**
```kotlin
versionCode 1
versionName "1.0"
```

**AI가 개선할 수 있음:**
```kotlin
versionCode getVersionCode()  // Git 커밋 수 기반
versionName "1.0.${getVersionCode()}"
```

### 4. 빌드 변형(Build Variants) 최적화
**현재 상태: Debug/Release만 있음** | **작업 시간: 15분**

- [ ] Staging 환경 추가
- [ ] 환경별 API 키 분리
- [ ] 빌드 타입별 앱 이름 구분

**AI가 추가할 수 있음:**
```kotlin
flavorDimensions "environment"
productFlavors {
    dev { }
    staging { }
    production { }
}
```

### 5. 테스트 커버리지 개선
**현재 상태: 테스트 없음** | **작업 시간: 1-2시간**

- [ ] 단위 테스트 작성
  - ViewModel 테스트
  - Repository 테스트
  - API 서비스 테스트
  
- [ ] UI 테스트 작성
  - Compose UI 테스트
  - 화면 전환 테스트

**AI가 할 일:**
- 각 클래스에 대한 기본 테스트 생성
- Mock 객체 설정
- 테스트 시나리오 작성

### 6. CI/CD 파이프라인 강화
**현재 상태: 기본 빌드만 있음** | **작업 시간: 30분**

- [ ] GitHub Actions 워크플로우 추가
  - [ ] 자동 릴리즈 빌드
  - [ ] APK 자동 업로드
  - [ ] 버전 태깅 자동화
  - [ ] Google Play에 자동 배포 (선택사항)

**AI가 추가할 수 있음:**
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease
  
- name: Sign APK
  uses: r0adkll/sign-android-release@v1

- name: Upload to Play Store
  uses: r0adkll/upload-google-play@v1
```

### 7. 앱 크기 최적화
**현재 상태: 15MB (Debug)** | **작업 시간: 30분**

- [ ] 리소스 최적화
  - 사용하지 않는 리소스 제거
  - 이미지 압축
  - 벡터 드로어블 사용

- [ ] APK 분석 및 최적화
  - APK Analyzer 사용
  - 불필요한 의존성 제거
  - App Bundle 생성 (권장)

**AI가 할 일:**
```kotlin
android {
    buildTypes {
        release {
            shrinkResources true
        }
    }
    
    // App Bundle 설정
    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
}
```

### 8. 보안 강화
**현재 상태: 기본 보안** | **작업 시간: 20분**

- [ ] API 키 암호화
  - BuildConfig에서 NDK로 이동
  - 난독화 강화

- [ ] 네트워크 보안 설정
  - HTTPS 강제 사용
  - 인증서 피닝 (선택사항)

**AI가 추가할 수 있음:**
```xml
<!-- network_security_config.xml -->
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
</network-security-config>
```

### 9. 성능 모니터링 추가
**현재 상태: 없음** | **작업 시간: 30분**

- [ ] Firebase Crashlytics 통합
  - 크래시 리포트 수집
  - ANR 추적

- [ ] Firebase Analytics 추가
  - 사용자 행동 추적
  - 화면 조회 추적

**AI가 추가할 수 있음:**
```kotlin
// build.gradle
implementation 'com.google.firebase:firebase-crashlytics'
implementation 'com.google.firebase:firebase-analytics'
```

### 10. 앱 업데이트 메커니즘
**현재 상태: 없음** | **작업 시간: 30분**

- [ ] In-App Updates API 통합
  - 강제 업데이트 기능
  - 선택적 업데이트 알림

**AI가 추가할 수 있음:**
```kotlin
implementation 'com.google.android.play:app-update:2.1.0'
implementation 'com.google.android.play:app-update-ktx:2.1.0'
```

### 11. 문서 자동 생성
**현재 상태: 수동 문서** | **작업 시간: 30분**

- [ ] 개인정보처리방침 템플릿 생성
  - 앱 기능 기반 자동 생성
  - 다국어 지원

- [ ] 이용약관 템플릿 생성
- [ ] README 업데이트
- [ ] 변경 로그(Changelog) 자동 생성

**AI가 할 일:**
- Markdown 형식 문서 생성
- GitHub Pages 설정
- 자동 배포 설정

### 12. 지역화(Localization) 추가
**현재 상태: 영어/한국어 일부** | **작업 시간: 1시간**

- [ ] 다국어 리소스 완성
  - [ ] 한국어 (ko)
  - [ ] 영어 (en)
  - [ ] 기타 언어 (선택사항)

- [ ] Play Store 설명 번역
- [ ] 스크린샷 다국어 버전

**AI가 할 일:**
```xml
<!-- values-ko/strings.xml -->
<resources>
    <string name="app_name">버블티</string>
    <!-- ... -->
</resources>
```

---

## 🟡 출시 후 필요한 작업

### 즉시 필요한 작업
- [ ] **Google Play Console 모니터링**
  - 크래시 리포트 확인
  - 사용자 리뷰 응답
  - ANR (Application Not Responding) 추적

- [ ] **성능 메트릭 확인**
  - Android Vitals 지표 모니터링
  - 배터리 소모량
  - 앱 시작 시간

### 단기 (1-2주)
- [ ] **사용자 피드백 반영**
  - 버그 수정
  - 기능 개선

- [ ] **첫 번째 업데이트 준비**
  - 버전 1.1 계획
  - 피드백 기반 개선사항

### 중기 (1-3개월)
- [ ] **기능 확장**
  - 버블티 주문 기능 실제 구현
  - 결제 시스템 통합
  - 사용자 계정 시스템

- [ ] **마케팅**
  - 소셜 미디어 홍보
  - 웹사이트 구축
  - 블로그 포스팅

---

## 📊 우선순위 요약

### 🔴 즉시 필요 (출시 전 필수)
1. Google Play Console 계정 생성 - **당신**
2. 개인정보처리방침 작성 및 호스팅 - **당신**
3. 앱 서명 키 생성 - **당신**
4. 스크린샷 및 마케팅 자료 준비 - **당신**
5. 앱 서명 설정 코드 추가 - **AI**
6. ProGuard 최적화 활성화 - **AI**

### 🟠 높은 우선순위 (출시 전 권장)
1. 실제 기기 테스트 완료 - **당신**
2. 콘텐츠 등급 완료 - **당신**
3. 데이터 보안 섹션 작성 - **당신**
4. 단위 테스트 추가 - **AI**
5. 앱 크기 최적화 - **AI**

### 🟡 중간 우선순위 (출시 후 가능)
1. NewsAPI 유료 플랜 검토 - **당신**
2. Firebase Crashlytics 추가 - **AI**
3. In-App Updates 추가 - **AI**

### 🟢 낮은 우선순위 (선택사항)
1. 홍보 비디오 제작 - **당신**
2. 다국어 지원 확장 - **AI**
3. 추가 빌드 변형 - **AI**

---

## 💰 예상 비용

### 필수 비용
- Google Play Console 등록: **$25** (일회성)
- NewsAPI 유료 플랜: **$449/월** 또는 무료 대안 찾기
- 앱 서명 인증서: **무료** (자체 생성)

### 선택사항 비용
- 개인정보처리방침 호스팅: **무료** (GitHub Pages 사용)
- Firebase 무료 플랜: **무료** (일정 한도까지)
- 도메인 구매: **$10-15/년** (선택사항)

### 총 예상 초기 비용
- **최소: $25** (NewsAPI 무료 플랜 유지 시)
- **권장: $474** (NewsAPI 유료 플랜 포함)

---

## ⏱️ 예상 소요 시간

### 당신의 작업 시간
- **최소 필수 작업**: 4-6시간
  - Play Console 설정: 1시간
  - 문서 작성: 2-3시간
  - 스크린샷 및 자료: 1-2시간
  - 테스트: 1시간

### AI의 작업 시간
- **기술 구현**: 2-3시간
  - 빌드 설정: 30분
  - 테스트 작성: 1시간
  - 최적화: 1시간
  - CI/CD: 30분

### 총 예상 시간
- **출시 준비 완료**: 6-9시간
- **검토 및 승인 대기**: 1-7일 (Google 검토)

---

## 🚀 시작하기

### 지금 바로 시작할 수 있는 것

1. **Google Play Console 계정 생성** (지금 바로!)
   - 링크: https://play.google.com/console/signup
   - 신용카드 준비
   - 24-48시간 후 승인

2. **AI에게 요청할 것들** (이 체크리스트 보여주기)
   ```
   "이 체크리스트의 'AI가 도와줄 수 있는 작업' 섹션을 
   우선순위 순서대로 구현해줘"
   ```

3. **개인정보처리방침 템플릿 요청**
   ```
   "BubbleTea 앱의 개인정보처리방침 초안을 작성해줘.
   NewsAPI, AdMob, 위젯 기능을 포함해서."
   ```

4. **마케팅 자료 작성 요청**
   ```
   "Google Play Store용 앱 설명을 한국어와 영어로 작성해줘.
   주요 기능: 기술 뉴스, 위젯, 오프라인 지원"
   ```

---

## 📞 도움이 필요한 경우

### Google Play 지원
- 도움말 센터: https://support.google.com/googleplay/android-developer
- 커뮤니티 포럼: https://support.google.com/googleplay/android-developer/community

### 개발 관련
- Android 개발자 가이드: https://developer.android.com/distribute
- Play Console 문서: https://support.google.com/googleplay/android-developer

---

## ✅ 다음 단계

이 문서를 확인했다면:

1. ✅ 우선순위 작업 목록 확인
2. ✅ 비용 및 시간 확인
3. ✅ Google Play Console 계정 생성 시작
4. ✅ AI에게 기술 작업 요청

**준비되셨나요? 시작해봅시다! 🚀**
