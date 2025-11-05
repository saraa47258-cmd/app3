package com.example.app3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.app3.data.model.ProductWithProgress
import com.example.app3.ui.components.EmptyStateView
import com.example.app3.ui.components.GradientCard
import com.example.app3.ui.components.PriorityChip
import com.example.app3.ui.theme.*
import com.example.app3.ui.viewmodel.WishListViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.app3.data.model.Product
import com.example.app3.data.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreen(
    viewModel: WishListViewModel,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToBudget: () -> Unit
) {
    val products by viewModel.products.collectAsStateWithLifecycle(initialValue = emptyList())
    val budget by viewModel.budget.collectAsStateWithLifecycle(initialValue = null)
    val totalValue by viewModel.totalValue.collectAsStateWithLifecycle(initialValue = 0.0)
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    var showSortMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<ProductWithProgress?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "قائمة أمنياتي",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "فرز")
                    }
                    
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("الأحدث") },
                            onClick = {
                                viewModel.setSortOption(WishListViewModel.SortOption.Recent)
                                showSortMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Schedule, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("الأولوية") },
                            onClick = {
                                viewModel.setSortOption(WishListViewModel.SortOption.Priority)
                                showSortMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.PriorityHigh, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("السعر") },
                            onClick = {
                                viewModel.setSortOption(WishListViewModel.SortOption.Price)
                                showSortMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.AttachMoney, null) }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddProduct,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("إضافة منتج") },
                containerColor = Primary
            )
        }
    ) { padding ->
        if (products.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.ShoppingBag,
                title = "قائمتك فارغة",
                subtitle = "ابدأ بإضافة المنتجات التي تحلم بشرائها",
                actionText = "إضافة منتج",
                onActionClick = onNavigateToAddProduct
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BudgetSummaryCard(
                        monthlySaving = budget?.monthlySaving ?: 0.0,
                        totalValue = totalValue,
                        currency = budget?.currency ?: "ر.س",
                        onSetupBudget = onNavigateToBudget
                    )
                }

                items(
                    items = products,
                    key = { it.product.id }
                ) { productWithProgress ->
                    ProductCard(
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

    // Delete confirmation dialog
    showDeleteDialog?.let { product ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("حذف المنتج") },
            text = { Text("هل أنت متأكد من حذف \"${product.product.name}\"؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(product.product)
                        showDeleteDialog = null
                    }
                ) {
                    Text("حذف", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun BudgetSummaryCard(
    monthlySaving: Double,
    totalValue: Double,
    currency: String,
    onSetupBudget: () -> Unit
) {
    GradientCard {
        Column {
            if (monthlySaving > 0) {
                // Header with icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "ملخص الميزانية",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    IconButton(onClick = onSetupBudget) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "تعديل",
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Monthly Saving
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "ادخار شهري",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${String.format("%.0f", monthlySaving)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            currency,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Total Value
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "إجمالي القائمة",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${String.format("%.0f", totalValue)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            currency,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Progress indicator
                if (totalValue > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val monthsNeeded = if (monthlySaving > 0) {
                        kotlin.math.ceil(totalValue / monthlySaving).toInt()
                    } else 0
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "وقت إنجاز كامل القائمة",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            "$monthsNeeded شهر",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Empty state
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "ابدأ بإعداد ميزانيتك",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "حدد دخلك الشهري لمعرفة متى يمكنك تحقيق أحلامك",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Surface(
                        onClick = onSetupBudget,
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "إعداد",
                                tint = Primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    productWithProgress: ProductWithProgress,
    currency: String,
    onMarkPurchased: (ProductWithProgress) -> Unit,
    onDelete: (ProductWithProgress) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val product = productWithProgress.product

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Product Image
                    if (product.imageUri != null) {
                        AsyncImage(
                            model = product.imageUri,
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.tertiaryContainer
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // Product Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${String.format("%.0f", product.price)} $currency",
                            style = MaterialTheme.typography.titleLarge,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        PriorityChip(priority = product.priority)
                    }
                }

                // Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "المزيد")
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
                            leadingIcon = { Icon(Icons.Default.CheckCircle, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("حذف") },
                            onClick = {
                                onDelete(productWithProgress)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Section
            if (productWithProgress.monthsNeeded > 0) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "الوقت المتوقع: ${productWithProgress.monthsNeeded} شهر",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            productWithProgress.estimatedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LinearProgressIndicator(
                        progress = { productWithProgress.progressPercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                }
            } else {
                Text(
                    "قم بتحديد ميزانيتك لمعرفة الوقت المتوقع",
                    style = MaterialTheme.typography.bodySmall,
                    color = Warning,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCardPreview() {
    val sampleProduct = Product(
        id = 1,
        name = "آيفون 15 برو",
        price = 5000.0,
        imageUri = null,
        category = "تقنية",
        priority = Priority.HIGH,
        productUrl = null,
        createdAt = System.currentTimeMillis(),
        isPurchased = false,
        purchasedDate = null,
        notes = null
    )
    
    val productWithProgress = ProductWithProgress(
        product = sampleProduct,
        monthsNeeded = 5,
        progressPercentage = 40f,
        estimatedDate = "مايو 2025"
    )
    
    WishListSmartTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                ProductCard(
                    productWithProgress = productWithProgress,
                    currency = "ر.س",
                    onMarkPurchased = {},
                    onDelete = {}
                )
            }
        }
    }
}

