# Architecture Overview

## Application Architecture

This application follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)** pattern for a maintainable and testable codebase.

### Layer Structure

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Composables, ViewModels, UI State)   │
├─────────────────────────────────────────┤
│           Domain Layer                  │
│       (Use Cases - Optional)            │
├─────────────────────────────────────────┤
│            Data Layer                   │
│   (Repository, Data Sources, Models)   │
└─────────────────────────────────────────┘
```

## Components Breakdown

### 1. Data Layer

#### Models
- **Product**: Core entity representing wishlist items
  - Properties: id, name, price, imageUri, category, priority, createdAt
  - Enum: Priority (HIGH, MEDIUM, LOW)
  
- **Budget**: Financial planning entity
  - Properties: monthlyIncome, monthlySaving, fixedExpenses, currency
  - Computed: availableForSaving

- **Offer**: Promotional offers entity
  - Properties: productName, discountPercentage, originalPrice, expiryDate

#### DAOs (Data Access Objects)
- **ProductDao**: CRUD operations for products
  - getAllProducts(), getProductsByPriority(), getProductsByPrice()
  - markAsPurchased(), getTotalWishlistValue()
  
- **BudgetDao**: Budget management
  - getBudget(), insertBudget(), updateBudget()
  
- **OfferDao**: Offers management
  - getAllActiveOffers(), deactivateExpiredOffers()

#### Database
- **WishListDatabase**: Room database singleton
  - Entities: Product, Budget, Offer
  - Type Converters: Priority enum conversion

#### Repository
- **WishListRepository**: Single source of truth
  - Aggregates data from multiple DAOs
  - Provides Flow-based reactive streams
  - Calculates ProductWithProgress combining products and budget

### 2. Presentation Layer

#### ViewModels

**WishListViewModel**
- State: products, budget, totalValue, sortOption
- Actions: setSortOption(), deleteProduct(), markAsPurchased()
- Uses StateFlow for reactive UI updates

**AddProductViewModel**
- State: form fields (name, price, category, priority, etc.)
- Validation: isFormValid()
- Actions: saveProduct(), updateField methods

**BudgetViewModel**
- State: budget fields, productsWithProgress
- Calculations: calculateSuggestedSaving()
- Actions: saveBudget()

**OffersViewModel**
- State: active offers list
- Actions: deleteOffer()
- Initialization: loads sample data, deactivates expired offers

#### Screens

**WishListScreen**
- Main dashboard showing all wishlist items
- Features: sorting, filtering, progress tracking
- Components: BudgetSummaryCard, ProductCard

**AddProductScreen**
- Form for adding new products
- Features: image picker, category selection, priority chips
- Validation: real-time form validation

**BudgetScreen**
- Budget configuration interface
- Features: circular progress indicator, timeline view
- Smart suggestions: 30% of available income

**OffersScreen**
- Displays promotional offers
- Features: discount badges, expiry dates, external links

#### Components
- **EmptyStateView**: Consistent empty states across the app
- **GradientCard**: Reusable card with gradient background
- **PriorityChip**: Visual priority indicator
- **LoadingView**: Standard loading indicator

### 3. Theme System

#### Color Palette
- Primary: #0A84FF (iOS-style blue)
- Secondary: #FFC857 (Warm yellow for offers)
- Success: #3FB984 (Green for achievements)
- Warning: #FF6F59 (Red for alerts)

#### Typography
- Display: 32sp/28sp/24sp (Bold/SemiBold)
- Headline: 24sp/20sp/18sp (SemiBold)
- Title: 20sp/16sp/14sp (SemiBold/Medium)
- Body: 16sp/14sp/12sp (Normal)

#### Components
- Card elevation: 2dp
- Corner radius: 12dp (cards), 8dp (chips)
- Spacing: 16dp standard, 12dp compact

### 4. Navigation

#### Routes
```kotlin
sealed class Screen(val route: String) {
    object WishList : Screen("wishlist")
    object AddProduct : Screen("add_product")
    object Budget : Screen("budget")
    object Offers : Screen("offers")
}
```

#### Navigation Structure
- Bottom Navigation: 3 main tabs (WishList, Budget, Offers)
- Stack Navigation: AddProduct as modal screen
- State preservation: saveState = true, restoreState = true

### 5. Business Logic

#### Progress Calculation
```kotlin
monthsNeeded = ceil(productPrice / monthlySaving)
progressPercentage = (monthsPassed / monthsNeeded) * 100
estimatedDate = createdAt + (monthsNeeded * 30 days)
```

#### Budget Suggestions
```kotlin
availableForSaving = monthlyIncome - fixedExpenses
suggestedSaving = availableForSaving * 0.3  // 30% rule
```

## Data Flow

### Reading Data
```
Database → DAO → Repository → ViewModel (Flow) → UI (collectAsState)
```

### Writing Data
```
UI → ViewModel → Repository → DAO → Database
```

### State Management
- **StateFlow**: For simple state (sort option, form fields)
- **Flow**: For database streams (products, budget)
- **collectAsStateWithLifecycle**: For lifecycle-aware collection

## Dependencies

### Core
- Kotlin 2.0.21
- Compose BOM 2024.10.00
- Material 3

### Architecture
- Lifecycle ViewModel Compose 2.8.7
- Navigation Compose 2.8.3
- Room 2.6.1

### Utilities
- Coil Compose 2.7.0 (Image loading)
- Coroutines Android 1.9.0

## Best Practices Implemented

1. **Single Source of Truth**: Repository pattern
2. **Separation of Concerns**: Clear layer boundaries
3. **Reactive Programming**: Flow-based data streams
4. **Lifecycle Awareness**: ViewModels and collectAsStateWithLifecycle
5. **Type Safety**: Sealed classes for navigation and state
6. **Immutability**: Data classes with val properties
7. **Error Handling**: Try-catch in ViewModels
8. **Memory Efficiency**: LazyColumn for lists, coil for images
9. **Accessibility**: Content descriptions, semantic properties
10. **Performance**: Remember, derivedStateOf, keys in LazyColumn

## Testing Strategy (Future)

### Unit Tests
- ViewModels: Business logic, state transformations
- Repository: Data operations, Flow transformations
- Utilities: Calculation functions

### Integration Tests
- DAOs: Database operations with in-memory database
- Repository: Combined data source operations

### UI Tests
- Compose Test: Screen interactions, navigation flows
- Screenshot Tests: Visual regression testing

## Performance Optimizations

1. **Database**
   - Indexed columns (primary keys)
   - Flow queries (no manual observation)
   - Single database instance

2. **UI**
   - LazyColumn instead of Column + ScrollView
   - Keys for stable item identity
   - Remember for expensive computations
   - Stable parameters in Composables

3. **Memory**
   - Image loading with Coil (caching, downsampling)
   - ViewModels for state preservation
   - StateFlow (hot stream, last value)

## Security Considerations

1. **Data Storage**: Room with encryption (future)
2. **Permissions**: POST_NOTIFICATIONS for Android 13+
3. **Input Validation**: Price, income validation
4. **SQL Injection**: Prevented by Room parameterized queries

## Scalability

### Current State
- Suitable for personal use (100s of items)
- Local-only storage
- No authentication

### Future Enhancements
- Cloud sync (Firebase Firestore)
- Multi-user support
- Pagination for large datasets
- Background sync with WorkManager
- Offline-first architecture


