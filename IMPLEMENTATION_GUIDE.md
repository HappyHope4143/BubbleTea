# Implementation Guide - MVVM + Compose + Hilt

## 🎯 Implementation Patterns

### 1. Screen Structure Pattern

#### Complete Screen Implementation Example

```kotlin
// 1. UI State Definition
sealed interface MenuUiState {
    object Loading : MenuUiState
    data class Success(
        val menuItems: List<MenuItem>,
        val selectedCategory: MenuCategory,
        val cartItemCount: Int
    ) : MenuUiState
    data class Error(val message: String) : MenuUiState
}

// 2. User Events Definition  
sealed interface MenuEvent {
    object LoadMenu : MenuEvent
    data class SelectCategory(val category: MenuCategory) : MenuEvent
    data class AddToCart(val item: MenuItem) : MenuEvent
    data class ToggleFavorite(val itemId: String) : MenuEvent
}

// 3. ViewModel Implementation
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenuItemsUseCase: GetMenuItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    
    init {
        handleEvent(MenuEvent.LoadMenu)
        observeCartCount()
    }
    
    fun handleEvent(event: MenuEvent) {
        when (event) {
            is MenuEvent.LoadMenu -> loadMenuItems()
            is MenuEvent.SelectCategory -> filterByCategory(event.category)
            is MenuEvent.AddToCart -> addItemToCart(event.item)
            is MenuEvent.ToggleFavorite -> toggleFavorite(event.itemId)
        }
    }
    
    private fun loadMenuItems() {
        viewModelScope.launch {
            getMenuItemsUseCase()
                .onStart { _uiState.value = MenuUiState.Loading }
                .catch { error -> 
                    _uiState.value = MenuUiState.Error(error.message ?: "Unknown error")
                }
                .collect { items ->
                    _uiState.value = MenuUiState.Success(
                        menuItems = items,
                        selectedCategory = MenuCategory.All,
                        cartItemCount = getCurrentCartCount()
                    )
                }
        }
    }
    
    private fun addItemToCart(item: MenuItem) {
        viewModelScope.launch {
            addToCartUseCase(item)
                .onSuccess { 
                    // Update cart count in current state
                    updateCartCount()
                }
                .onFailure { error ->
                    // Handle error (show snackbar, etc.)
                }
        }
    }
    
    // ... other methods
}

// 4. Screen Composable
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onNavigateToCart: () -> Unit,
    onNavigateToItemDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    MenuContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
        onNavigateToCart = onNavigateToCart,
        onNavigateToItemDetail = onNavigateToItemDetail
    )
}

// 5. Content Composable (Pure UI)
@Composable
private fun MenuContent(
    uiState: MenuUiState,
    onEvent: (MenuEvent) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToItemDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar with cart icon
        MenuTopBar(
            cartItemCount = when (uiState) {
                is MenuUiState.Success -> uiState.cartItemCount
                else -> 0
            },
            onCartClick = onNavigateToCart
        )
        
        when (uiState) {
            is MenuUiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is MenuUiState.Success -> {
                MenuSuccessContent(
                    menuItems = uiState.menuItems,
                    selectedCategory = uiState.selectedCategory,
                    onEvent = onEvent,
                    onItemClick = onNavigateToItemDetail
                )
            }
            is MenuUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetry = { onEvent(MenuEvent.LoadMenu) }
                )
            }
        }
    }
}
```

### 2. Use Case Pattern

```kotlin
// Domain Use Case Interface
interface GetMenuItemsUseCase {
    operator fun invoke(): Flow<List<MenuItem>>
}

// Implementation
@Singleton
class GetMenuItemsUseCaseImpl @Inject constructor(
    private val menuRepository: MenuRepository
) : GetMenuItemsUseCase {
    
    override fun invoke(): Flow<List<MenuItem>> {
        return menuRepository.getMenuItems()
            .map { items ->
                items.sortedBy { it.name }
            }
            .flowOn(Dispatchers.IO)
    }
}

// DI Module
@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetMenuItemsUseCase(
        impl: GetMenuItemsUseCaseImpl
    ): GetMenuItemsUseCase
}
```

### 3. Repository Pattern

```kotlin
// Domain Interface
interface MenuRepository {
    suspend fun getMenuItems(): Flow<List<MenuItem>>
    suspend fun addToCart(item: MenuItem): Result<Unit>
    suspend fun getCartItemCount(): Flow<Int>
}

// Data Implementation
@Singleton
class MenuRepositoryImpl @Inject constructor(
    private val apiService: BubbleTeaApiService,
    private val localDataSource: MenuLocalDataSource,
    private val mapper: MenuDataMapper
) : MenuRepository {
    
    override suspend fun getMenuItems(): Flow<List<MenuItem>> {
        return flow {
            // Try local first
            val localItems = localDataSource.getMenuItems()
            if (localItems.isNotEmpty()) {
                emit(mapper.mapToDomain(localItems))
            }
            
            // Fetch from network
            try {
                val networkItems = apiService.getMenuItems()
                localDataSource.saveMenuItems(networkItems)
                emit(mapper.mapToDomain(networkItems))
            } catch (e: Exception) {
                if (localItems.isEmpty()) {
                    throw e
                }
                // Continue with cached data if network fails
            }
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun addToCart(item: MenuItem): Result<Unit> {
        return try {
            localDataSource.addToCart(mapper.mapToData(item))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 4. Navigation Pattern

```kotlin
// Navigation Routes
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Menu : Screen("menu")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    data class ItemDetail(val itemId: String) : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: String) = "item_detail/$itemId"
    }
}

// Navigation Setup
@Composable
fun BubbleTeaNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMenu = {
                    navController.navigate(Screen.Menu.route)
                }
            )
        }
        
        composable(Screen.Menu.route) {
            MenuScreen(
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToItemDetail = { itemId ->
                    navController.navigate(Screen.ItemDetail("").createRoute(itemId))
                }
            )
        }
        
        composable(
            route = Screen.ItemDetail("").route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ItemDetailScreen(
                itemId = itemId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
```

## 🎨 Compose Best Practices

### 1. Component Structure

```kotlin
// Reusable Component Pattern
@Composable
fun BubbleTeaCard(
    item: MenuItem,
    onAddToCart: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₩${item.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (item.isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Toggle favorite",
                            tint = if (item.isFavorite) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                    
                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add to Cart")
                    }
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
private fun BubbleTeaCardPreview() {
    BubbleTeaTheme {
        BubbleTeaCard(
            item = MenuItem(
                id = "1",
                name = "Classic Milk Tea",
                description = "Traditional milk tea with tapioca pearls",
                price = 5500,
                imageUrl = "",
                isFavorite = false
            ),
            onAddToCart = { },
            onToggleFavorite = { }
        )
    }
}
```

### 2. Theme System

```kotlin
// Design System Values
object BubbleTeaDesignTokens {
    val Spacing = BubbleTeaSpacing
    val Typography = BubbleTeaTypography
    val Colors = BubbleTeaColors
}

object BubbleTeaSpacing {
    val xs = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val xl = 32.dp
}

// Custom Material 3 Theme
@Composable
fun BubbleTeaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

## 🔌 Hilt Setup Guide

### 1. Project Setup

```kotlin
// build.gradle (Project)
buildscript {
    dependencies {
        classpath "com.google.dagger:hilt-android-gradle-plugin:2.48"
    }
}

// build.gradle (App)
plugins {
    id 'dagger.hilt.android.plugin'
    id 'kotlin-kapt'
}

dependencies {
    implementation "com.google.dagger:hilt-android:2.48"
    kapt "com.google.dagger:hilt-compiler:2.48"
    
    // For ViewModels
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
}
```

### 2. Application Class

```kotlin
@HiltAndroidApp
class BubbleTeaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Application initialization
    }
}
```

### 3. Module Organization

```kotlin
// Network Module
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.bubbletea.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideBubbleTeaApiService(retrofit: Retrofit): BubbleTeaApiService {
        return retrofit.create(BubbleTeaApiService::class.java)
    }
}

// Database Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BubbleTeaDatabase {
        return Room.databaseBuilder(
            context,
            BubbleTeaDatabase::class.java,
            "bubbletea_database"
        ).build()
    }
    
    @Provides
    fun provideMenuDao(database: BubbleTeaDatabase): MenuDao {
        return database.menuDao()
    }
}

// Repository Module
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindMenuRepository(
        menuRepositoryImpl: MenuRepositoryImpl
    ): MenuRepository
    
    @Binds
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository
}
```

## 🧪 Testing Strategy

### 1. ViewModel Testing

```kotlin
@ExperimentalCoroutinesTest
class MenuViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    @Mock
    private lateinit var getMenuItemsUseCase: GetMenuItemsUseCase
    
    @Mock
    private lateinit var addToCartUseCase: AddToCartUseCase
    
    private lateinit var viewModel: MenuViewModel
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MenuViewModel(getMenuItemsUseCase, addToCartUseCase)
    }
    
    @Test
    fun `when loadMenu succeeds, should emit success state`() = runTest {
        // Given
        val menuItems = listOf(
            MenuItem(id = "1", name = "Tea 1", price = 5000),
            MenuItem(id = "2", name = "Tea 2", price = 6000)
        )
        whenever(getMenuItemsUseCase()).thenReturn(flowOf(menuItems))
        
        // When
        viewModel.handleEvent(MenuEvent.LoadMenu)
        
        // Then
        val uiState = viewModel.uiState.value
        assertTrue(uiState is MenuUiState.Success)
        assertEquals(menuItems, (uiState as MenuUiState.Success).menuItems)
    }
}
```

### 2. Compose UI Testing

```kotlin
@HiltAndroidTest
class MenuScreenTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    
    @Before
    fun setUp() {
        hiltRule.inject()
    }
    
    @Test
    fun menuScreen_displaysMenuItems() {
        // Given
        val menuItems = listOf(
            MenuItem(id = "1", name = "Classic Milk Tea", price = 5500),
            MenuItem(id = "2", name = "Taro Milk Tea", price = 6000)
        )
        
        // When
        composeTestRule.setContent {
            BubbleTeaTheme {
                MenuContent(
                    uiState = MenuUiState.Success(
                        menuItems = menuItems,
                        selectedCategory = MenuCategory.All,
                        cartItemCount = 0
                    ),
                    onEvent = { },
                    onNavigateToCart = { },
                    onNavigateToItemDetail = { }
                )
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("Classic Milk Tea").assertIsDisplayed()
        composeTestRule.onNodeWithText("Taro Milk Tea").assertIsDisplayed()
        composeTestRule.onNodeWithText("₩5,500").assertIsDisplayed()
    }
}
```

---

*This implementation guide provides concrete patterns and examples for implementing the BubbleTea app architecture.*