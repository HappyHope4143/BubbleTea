# BubbleTea App - Architecture & Module Structure

## 📋 Overview

This document outlines the architectural decisions and module structure for the BubbleTea Android application, defining the implementation of MVVM pattern with Jetpack Compose and dependency injection strategy.

## 🏗️ Module Structure Design

### Current State
- **Single Module**: `app` module containing all application logic

### Proposed Multi-Module Architecture

```
BubbleTea/
├── app/                    # Main application module
├── feature/
│   ├── home/              # Home screen feature
│   ├── menu/              # Menu browsing feature  
│   ├── cart/              # Shopping cart feature
│   ├── profile/           # User profile feature
│   └── ordering/          # Order management feature
├── core/
│   ├── ui/                # Shared UI components and theme
│   ├── data/              # Data layer (repositories, data sources)
│   ├── domain/            # Business logic and use cases
│   ├── network/           # Network layer and API clients
│   └── common/            # Shared utilities and extensions
├── shared/
│   └── resources/         # Shared resources (strings, drawables)
└── buildSrc/              # Build logic and dependency management
```

### Module Responsibilities

#### App Module (`app`)
- Application entry point and DI setup
- Navigation host and main activity
- Application-level configurations
- Minimal business logic

#### Feature Modules (`feature/*`)
- Self-contained feature implementations
- Feature-specific UI screens and components
- Feature ViewModels and navigation
- Feature-specific resources

#### Core Modules (`core/*`)

**core:ui**
- Shared Compose components and theme system
- Design system implementation
- Common UI utilities and extensions
- Reusable Composables

**core:data**
- Repository implementations
- Data source abstractions
- Local database and cache management
- Data mapping and transformation

**core:domain**
- Business logic and use cases
- Entity definitions
- Repository interfaces
- Domain-specific exceptions

**core:network**
- API client implementations
- Network models and DTOs
- Retrofit configurations
- Network error handling

**core:common**
- Shared utilities and helper functions
- Common extensions
- Constants and configurations
- Base classes and interfaces

## 🎯 MVVM + Compose Architecture

### Architecture Pattern
**Selected**: **MVVM (Model-View-ViewModel)** with **Unidirectional Data Flow**

### Implementation Strategy

#### View Layer (Composables)
```kotlin
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    MenuContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
        onNavigateToCart = onNavigateToCart
    )
}

@Composable
private fun MenuContent(
    uiState: MenuUiState,
    onEvent: (MenuEvent) -> Unit,
    onNavigateToCart: () -> Unit
) {
    // Pure Composable UI implementation
}
```

#### ViewModel Layer
```kotlin
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenuItemsUseCase: GetMenuItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    
    fun handleEvent(event: MenuEvent) {
        when (event) {
            is MenuEvent.LoadMenu -> loadMenuItems()
            is MenuEvent.AddToCart -> addItemToCart(event.item)
        }
    }
}
```

#### Model Layer (Domain + Data)
```kotlin
// Domain Layer
interface MenuRepository {
    suspend fun getMenuItems(): Flow<List<MenuItem>>
    suspend fun addToCart(item: MenuItem): Result<Unit>
}

// Data Layer Implementation
@Singleton
class MenuRepositoryImpl @Inject constructor(
    private val apiService: BubbleTeaApiService,
    private val localDataSource: MenuLocalDataSource
) : MenuRepository {
    // Implementation details
}
```

### State Management
- **UI State**: Sealed classes or data classes for screen states
- **Events**: Sealed classes for user interactions
- **Side Effects**: Handled through dedicated effect handlers

### Compose Application Scope

#### Full Compose Adoption
- **100% Jetpack Compose** for all UI components
- **No XML layouts** except for widgets/notifications
- **Material 3 Design System** throughout the app

#### Compose Usage Guidelines
1. **Screen Composables**: Top-level screen functions
2. **Component Composables**: Reusable UI components
3. **Preview Composables**: Development and testing previews
4. **Theme Integration**: Consistent Material 3 theming

## 🔧 Dependency Injection Framework

### Selected Framework: **Hilt**

### Decision Rationale

#### Why Hilt over Koin?

✅ **Hilt Advantages:**
- **Compile-time safety** and validation
- **Better Android integration** (ViewModels, WorkManager, etc.)
- **Annotation processing** catches errors early
- **Google-supported** and actively maintained
- **Better IDE support** and debugging
- **Performance benefits** through compile-time generation

❌ **Koin Limitations:**
- Runtime dependency resolution
- Less type safety
- Potential runtime crashes for misconfiguration
- Limited Android-specific integrations

### Hilt Implementation Strategy

#### Application Setup
```kotlin
@HiltAndroidApp
class BubbleTeaApplication : Application()
```

#### Module Organization
```kotlin
// Network Module
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(): BubbleTeaApiService = // implementation
}

// Repository Module  
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository
}

// Database Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BubbleTeaDatabase = // implementation
}
```

#### Feature Module DI
```kotlin
// Feature-specific module
@Module
@InstallIn(ViewModelComponent::class)
abstract class MenuModule {
    @Binds
    abstract fun bindMenuUseCase(impl: GetMenuItemsUseCaseImpl): GetMenuItemsUseCase
}
```

## 📐 Implementation Guidelines

### Module Dependencies
- **Feature modules** can depend on **core modules** only
- **Core modules** should not depend on **feature modules**
- **Circular dependencies** are strictly prohibited
- Use **dependency inversion** for cross-module communication

### Naming Conventions
- **Modules**: kebab-case (e.g., `feature-menu`, `core-data`)
- **Packages**: snake_case (e.g., `menu_screen`, `data_source`)
- **Classes**: PascalCase with descriptive suffixes
- **Composables**: PascalCase ending with purpose (e.g., `MenuScreen`, `CartButton`)

### Testing Strategy
- **Unit Tests**: ViewModels, UseCases, Repositories
- **Integration Tests**: Module interactions
- **UI Tests**: Critical user flows with Compose Testing
- **Screenshot Tests**: UI consistency validation

### Build Optimization
- **Parallel builds** enabled for multi-module structure
- **Build cache** optimization
- **Incremental compilation** for faster development
- **Feature module isolation** for targeted builds

## 🚀 Migration Path

### Phase 1: Core Module Setup
1. Create core modules (ui, data, domain, network, common)
2. Move shared components to core:ui
3. Extract data layer to core:data
4. Set up Hilt dependency injection

### Phase 2: Feature Extraction
1. Create feature modules for major screens
2. Migrate screens to feature modules
3. Implement MVVM pattern consistently
4. Add comprehensive testing

### Phase 3: Optimization
1. Optimize build performance
2. Add advanced DI features
3. Implement advanced state management
4. Performance monitoring and optimization

## 📊 Benefits of This Architecture

1. **Scalability**: Easy to add new features without affecting existing code
2. **Maintainability**: Clear separation of concerns and modular structure  
3. **Testability**: Each module can be tested independently
4. **Performance**: Faster build times through parallel compilation
5. **Team Development**: Multiple developers can work on different modules simultaneously
6. **Reusability**: Core modules can be reused across different parts of the app
7. **Type Safety**: Compile-time verification through Hilt

## 🔄 Future Considerations

- **Dynamic Feature Modules**: For advanced app size optimization
- **Compose Multiplatform**: Potential expansion to other platforms
- **Modularization Tools**: Gradle dependency analysis and optimization
- **Architecture Components**: Integration with newer Android Architecture Components

---

*This architecture document serves as the foundation for BubbleTea app development and should be updated as the project evolves.*