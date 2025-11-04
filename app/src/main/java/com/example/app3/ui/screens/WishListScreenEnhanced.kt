package com.example.app3.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.app3.data.model.ProductWithProgress
import com.example.app3.ui.components.EmptyStateView
import com.example.app3.ui.components.PriorityChip
import com.example.app3.ui.theme.*
import com.example.app3.ui.viewmodel.WishListViewModel
import kotlin.math.abs
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreenEnhanced(
    viewModel: WishListViewModel,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToBudget: () -> Unit
) {
    val products by viewModel.products.collectAsStateWithLifecycle(initialValue = emptyList())
    val budget by viewModel.budget.collectAsStateWithLifecycle(initialValue = null)
    val totalValue by viewModel.totalValue.collectAsStateWithLifecycle(initialValue = 0.0)
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle(initialValue = 0)

    var showSortMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<ProductWithProgress?>(null) }
    var selectedFilter by remember { mutableStateOf("الكل") }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "shine")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Scaffold(
        topBar = {
            EnhancedTopBar(
                productCount = productCount,
                onSortClick = { showSortMenu = true },
                showSortMenu = showSortMenu,
                onDismissSortMenu = { showSortMenu = false },
                sortOption = sortOption,
                onSortOptionSelected = { option ->
                    viewModel.setSortOption(option)
                    showSortMenu = false
                }
            )
        },
        floatingActionButton = {
            AnimatedFAB(onClick = onNavigateToAddProduct)
        },
        containerColor = Background
    ) { padding ->
        if (products.isEmpty()) {
            EnhancedEmptyState(
                onAddClick = onNavigateToAddProduct,
                onSetupBudgetClick = onNavigateToBudget
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Budget Card
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        EnhancedBudgetCard(
                            monthlySaving = budget?.monthlySaving ?: 0.0,
                            totalValue = totalValue,
                            productCount = productCount,
                            currency = budget?.currency ?: "ر.س",
                            onSetupBudget = onNavigateToBudget,
                            shimmerTranslate = shimmerTranslate
                        )
                    }
                }

                // Quick Stats
                item {
                    QuickStatsRow(
                        products = products,
                        budget = budget
                    )
                }

                // Filter Chips
                item {
                    FilterChipsRow(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it }
                    )
                }

                // Section Header
                item {
                    SectionHeader(
                        title = "قائمة أمنياتك",
                        count = products.size
                    )
                }

                // Products
                items(
                    items = products,
                    key = { it.product.id }
                ) { productWithProgress ->
                    EnhancedProductCard(
                        productWithProgress = productWithProgress,
                        currency = budget?.currency ?: "ر.س",
                        onMarkPurchased = { viewModel.markAsPurchased(it.product.id) },
                        onDelete = { showDeleteDialog = it }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Delete Dialog
    showDeleteDialog?.let { product ->
        DeleteConfirmationDialog(
            productName = product.product.name,
            onConfirm = {
                viewModel.deleteProduct(product.product)
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTopBar(
    productCount: Int,
    onSortClick: () -> Unit,
    showSortMenu: Boolean,
    onDismissSortMenu: () -> Unit,
    sortOption: WishListViewModel.SortOption,
    onSortOptionSelected: (WishListViewModel.SortOption) -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "قائمة أمنياتي",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$productCount منتج",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        },
        actions = {
            Box {
                IconButton(onClick = onSortClick) {
                    Surface(
                        shape = CircleShape,
                        color = if (showSortMenu) Primary.copy(alpha = 0.15f) else Color.Transparent
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "فرز",
                            modifier = Modifier.padding(8.dp),
                            tint = if (showSortMenu) Primary else OnBackground
                        )
                    }
                }
                
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = onDismissSortMenu
                ) {
                    DropdownMenuItem(
                        text = { Text("الأحدث") },
                        onClick = { onSortOptionSelected(WishListViewModel.SortOption.Recent) },
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        trailingIcon = {
                            if (sortOption is WishListViewModel.SortOption.Recent) {
                                Icon(Icons.Default.Check, null, tint = Primary)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("الأولوية") },
                        onClick = { onSortOptionSelected(WishListViewModel.SortOption.Priority) },
                        leadingIcon = { Icon(Icons.Default.PriorityHigh, null) },
                        trailingIcon = {
                            if (sortOption is WishListViewModel.SortOption.Priority) {
                                Icon(Icons.Default.Check, null, tint = Primary)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("السعر") },
                        onClick = { onSortOptionSelected(WishListViewModel.SortOption.Price) },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                        trailingIcon = {
                            if (sortOption is WishListViewModel.SortOption.Price) {
                                Icon(Icons.Default.Check, null, tint = Primary)
                            }
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun EnhancedBudgetCard(
    monthlySaving: Double,
    totalValue: Double,
    productCount: Int,
    currency: String,
    onSetupBudget: () -> Unit,
    shimmerTranslate: Float
) {
    var isExpanded by remember { mutableStateOf(true) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            GradientStart,
                            GradientMiddle,
                            GradientEnd
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(shimmerTranslate, shimmerTranslate)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                if (monthlySaving > 0) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.25f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "ملخص مالي",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    "تحديث مباشر",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        Row {
                            Surface(
                                onClick = onSetupBudget,
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "تعديل",
                                    tint = Color.White,
                                    modifier = Modifier.padding(8.dp).size(20.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Surface(
                                onClick = { isExpanded = !isExpanded },
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "توسيع/طي",
                                    tint = Color.White,
                                    modifier = Modifier.padding(8.dp).size(20.dp)
                                )
                            }
                        }
                    }
                    
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Stats Grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Monthly Saving
                                StatBox(
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Default.TrendingUp,
                                    label = "ادخار شهري",
                                    value = String.format("%.0f", monthlySaving),
                                    unit = currency,
                                    color = Color.White
                                )
                                
                                // Total Value
                                StatBox(
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Default.ShoppingCart,
                                    label = "إجمالي القائمة",
                                    value = String.format("%.0f", totalValue),
                                    unit = currency,
                                    color = Color.White
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Progress Info
                            if (totalValue > 0) {
                                val monthsNeeded = if (monthlySaving > 0) {
                                    kotlin.math.ceil(totalValue / monthlySaving).toInt()
                                } else 0
                                
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Timer,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                "وقت إنجاز كامل القائمة",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                        }
                                        
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White
                                        ) {
                                            Text(
                                                "$monthsNeeded شهر",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Primary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Setup Budget CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "ابدأ رحلتك المالية",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "حدد ميزانيتك وشاهد أحلامك تتحقق",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Surface(
                            onClick = onSetupBudget,
                            shape = CircleShape,
                            color = Color.White
                        ) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "ابدأ",
                                tint = Primary,
                                modifier = Modifier.padding(12.dp).size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    unit: String,
    color: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color.copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = color.copy(alpha = 0.8f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = color.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
fun QuickStatsRow(
    products: List<ProductWithProgress>,
    budget: com.example.app3.data.model.Budget?
) {
    val highPriority = products.count { it.product.priority == com.example.app3.data.model.Priority.HIGH }
    val nearestProduct = products.minByOrNull { it.monthsNeeded }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // High Priority Count
        QuickStatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.PriorityHigh,
            label = "أولوية عالية",
            value = "$highPriority",
            color = Error
        )
        
        // Nearest Goal
        nearestProduct?.let {
            QuickStatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Flag,
                label = "أقرب هدف",
                value = "${it.monthsNeeded} شهر",
                color = Success
            )
        }
    }
}

@Composable
fun QuickStatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.2f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(8.dp).size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    value,
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("الكل", "عالية", "متوسطة", "منخفضة")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                leadingIcon = if (selectedFilter == filter) {
                    { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                } else null,
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary.copy(alpha = 0.15f),
                    selectedLabelColor = Primary
                )
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Primary.copy(alpha = 0.1f)
        ) {
            Text(
                "$count منتج",
                style = MaterialTheme.typography.labelMedium,
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProductCard(
    productWithProgress: ProductWithProgress,
    currency: String,
    onMarkPurchased: (ProductWithProgress) -> Unit,
    onDelete: (ProductWithProgress) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val product = productWithProgress.product
    
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Image with gradient overlay
                    Box(
                        modifier = Modifier.size(90.dp)
                    ) {
                        if (product.imageUri != null) {
                            AsyncImage(
                                model = product.imageUri,
                                contentDescription = product.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Primary.copy(alpha = 0.3f),
                                                Secondary.copy(alpha = 0.3f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = Primary.copy(alpha = 0.6f)
                                )
                            }
                        }
                        
                        // Priority Badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp),
                            shape = CircleShape,
                            color = when (product.priority) {
                                com.example.app3.data.model.Priority.HIGH -> Error
                                com.example.app3.data.model.Priority.MEDIUM -> Warning
                                com.example.app3.data.model.Priority.LOW -> Success
                            }
                        ) {
                            Box(modifier = Modifier.size(8.dp))
                        }
                    }
                    
                    // Product Info
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = String.format("%.0f", product.price),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currency,
                                style = MaterialTheme.typography.bodySmall,
                                color = Primary.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        
                        PriorityChip(priority = product.priority)
                    }
                }
                
                // Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, "المزيد")
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("تم الشراء") },
                            onClick = {
                                onMarkPurchased(productWithProgress)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.CheckCircle, null, tint = Success)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("حذف") },
                            onClick = {
                                onDelete(productWithProgress)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, null, tint = Error)
                            }
                        )
                    }
                }
            }
            
            if (productWithProgress.monthsNeeded > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = TextSecondary
                            )
                            Text(
                                "${productWithProgress.monthsNeeded} شهر",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Text(
                            productWithProgress.estimatedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(productWithProgress.progressPercentage / 100f)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Primary, Secondary)
                                    )
                                )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        "${String.format("%.0f", productWithProgress.progressPercentage)}% مكتمل",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedFAB(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "fab_scale"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)
            .shadow(12.dp, CircleShape),
        containerColor = Primary,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Add, "إضافة", modifier = Modifier.size(24.dp))
            Text(
                "إضافة منتج",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EnhancedEmptyState(
    onAddClick: () -> Unit,
    onSetupBudgetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated Icon
        val infiniteTransition = rememberInfiniteTransition(label = "float")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -20f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "float_animation"
        )
        
        Icon(
            Icons.Default.CardGiftcard,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer { translationY = offsetY },
            tint = Primary.copy(alpha = 0.3f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            "ابدأ رحلة أحلامك",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "أضف المنتجات التي تحلم بها وشاهدها تتحقق خطوة بخطوة",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "إضافة أول منتج",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onSetupBudgetClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.AccountBalance, null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "إعداد الميزانية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    productName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = Error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                "حذف المنتج",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("هل أنت متأكد من حذف \"$productName\"؟\nلا يمكن التراجع عن هذا الإجراء.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error
                )
            ) {
                Text("حذف")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}



