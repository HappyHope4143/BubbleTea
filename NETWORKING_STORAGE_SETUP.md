# 네트워킹 및 로컬 저장소 구성 문서

## 개요
버블티 앱의 네트워크 통신 및 로컬 데이터 저장소 환경을 구축했습니다.

## 구현된 기능

### 📡 네트워킹 (Networking)
- **Retrofit 2.9.0**: REST API 통신
- **OkHttp 4.12.0**: HTTP 클라이언트 및 로깅 인터셉터
- **Kotlinx Serialization 1.6.2**: JSON 직렬화/역직렬화

### 💾 로컬 저장소 (Local Storage)
- **Room 2.6.1**: SQLite 데이터베이스 ORM
- **DataStore 1.0.0**: 사용자 설정 저장

### 🏗️ 아키텍처

#### 패키지 구조
```
com.happyhope.bubbletea.data/
├── model/              # 데이터 모델
│   ├── TeaProduct.kt   # 버블티 제품 모델
│   └── Order.kt        # 주문 모델
├── network/            # 네트워크 레이어
│   ├── BubbleTeaApiService.kt    # API 서비스 인터페이스
│   └── NetworkClient.kt          # Retrofit 클라이언트 설정
├── local/              # 로컬 저장소 레이어
│   ├── BubbleTeaDatabase.kt      # Room 데이터베이스
│   ├── TeaProductDao.kt          # 제품 데이터 접근 객체
│   ├── OrderDao.kt               # 주문 데이터 접근 객체
│   ├── Converters.kt             # Room 타입 컨버터
│   └── UserPreferencesDataStore.kt # 사용자 설정 저장소
└── repository/         # 저장소 레이어
    ├── TeaProductRepository.kt     # 제품 저장소 인터페이스
    ├── TeaProductRepositoryImpl.kt # 제품 저장소 구현체
    ├── OrderRepository.kt          # 주문 저장소 인터페이스
    └── OrderRepositoryImpl.kt      # 주문 저장소 구현체
```

## 🗄️ 데이터베이스 구조

### 테이블: tea_products
| 컬럼명 | 타입 | 설명 |
|---------|------|------|
| id | String (PK) | 제품 고유 ID |
| name | String | 제품명 |
| description | String | 제품 설명 |
| price | Double | 가격 |
| imageUrl | String | 이미지 URL |
| category | String | 카테고리 |
| isAvailable | Boolean | 판매 가능 여부 |
| ingredients | List<String> | 재료 목록 |

### 테이블: orders
| 컬럼명 | 타입 | 설명 |
|---------|------|------|
| id | String (PK) | 주문 고유 ID |
| productId | String | 제품 ID |
| productName | String | 제품명 |
| quantity | Int | 수량 |
| totalPrice | Double | 총 가격 |
| orderTime | Long | 주문 시간 (timestamp) |
| status | OrderStatus | 주문 상태 |
| customizations | List<String> | 커스터마이징 옵션 |

## 🔧 API 엔드포인트

### 제품 관련
- `GET /products` - 모든 제품 조회
- `GET /products?category={category}` - 카테고리별 제품 조회
- `GET /products/{id}` - 특정 제품 상세 조회

### 주문 관련
- `POST /orders` - 새 주문 생성
- `GET /orders?userId={userId}` - 사용자 주문 내역 조회
- `PUT /orders/{id}` - 주문 상태 업데이트
- `DELETE /orders/{id}` - 주문 취소

## 📱 사용자 설정 (DataStore)
- `user_name`: 사용자 이름
- `favorite_category`: 선호 카테고리
- `notifications_enabled`: 알림 활성화 여부
- `dark_mode_enabled`: 다크 모드 활성화 여부

## 🔄 Repository 패턴
- **TeaProductRepository**: 제품 데이터 관리
  - 로컬 데이터베이스에서 제품 조회
  - 서버에서 최신 제품 정보 동기화
- **OrderRepository**: 주문 데이터 관리
  - 로컬 주문 저장 및 서버 동기화
  - 주문 상태 관리 및 업데이트

## 🛠️ 기술적 특징
- **코루틴 지원**: 모든 네트워크 및 데이터베이스 작업에 코루틴 사용
- **Flow 기반**: 반응형 데이터 스트림으로 실시간 UI 업데이트
- **에러 핸들링**: Result 타입을 통한 안전한 에러 처리
- **타입 안전성**: Kotlinx Serialization으로 컴파일 타임 타입 검증

## 📋 Dependencies
```gradle
// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2'

// Local Storage
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'
implementation 'androidx.datastore:datastore-preferences:1.0.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
```

## 🚀 다음 단계
1. ViewModel과 UseCase 레이어 구현
2. UI 컴포넌트와 데이터 바인딩
3. 오프라인 캐싱 전략 구현
4. 단위 테스트 및 통합 테스트 작성