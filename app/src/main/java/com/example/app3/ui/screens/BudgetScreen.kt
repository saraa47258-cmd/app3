package com.example.app3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app3.ui.components.GradientCard
import com.example.app3.ui.theme.*
import com.example.app3.ui.viewmodel.BudgetViewModel
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    onNavigateBack: () -> Unit
) {
    val monthlyIncome by viewModel.monthlyIncome.collectAsStateWithLifecycle()
    val monthlySaving by viewModel.monthlySaving.collectAsStateWithLifecycle()
    val fixedExpenses by viewModel.fixedExpenses.collectAsStateWithLifecycle()
    val currency by viewModel.currency.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val productsWithProgress by viewModel.productsWithProgress.collectAsStateWithLifecycle()

    var showSuccessSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSuccessSnackbar) {
        if (showSuccessSnackbar) {
            snackbarHostState.showSnackbar("تم حفظ الميزانية بنجاح")
            showSuccessSnackbar = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccountBalance,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "الميزانية الشهرية",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Progress Circle Card
            val income = monthlyIncome.toDoubleOrNull() ?: 0.0
            val expenses = fixedExpenses.toDoubleOrNull() ?: 0.0
            val saving = monthlySaving.toDoubleOrNull() ?: 0.0
            val available = income - expenses
            val progressPercentage = if (income > 0 && available > 0) {
                ((saving / available) * 100.0).coerceIn(0.0, 100.0).toFloat()
            } else {
                0f
            }

            GradientCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "نسبة الادخار",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progressPercentage / 100f },
                            modifier = Modifier.size(120.dp),
                            color = Color.White,
                            strokeWidth = 12.dp,
                            trackColor = Color.White.copy(alpha = 0.3f),
                        )
                        
                        Text(
                            "${String.format("%.0f", progressPercentage)}%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (available > 0 && saving > 0) {
                        Text(
                            "${String.format("%.0f", saving)} من ${String.format("%.0f", available)} $currency متاح",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Input Fields Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "تفاصيل الميزانية",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = monthlyIncome,
                        onValueChange = { viewModel.updateMonthlyIncome(it) },
                        label = { Text("الدخل الشهري *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.AccountBalance, contentDescription = null)
                        },
                        suffix = { Text(currency) }
                    )

                    OutlinedTextField(
                        value = fixedExpenses,
                        onValueChange = { viewModel.updateFixedExpenses(it) },
                        label = { Text("المصروفات الثابتة (اختياري)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Receipt, contentDescription = null)
                        },
                        suffix = { Text(currency) }
                    )

                    OutlinedTextField(
                        value = monthlySaving,
                        onValueChange = { viewModel.updateMonthlySaving(it) },
                        label = { Text("الادخار الشهري *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Savings, contentDescription = null)
                        },
                        suffix = { Text(currency) }
                    )

                    // Suggestion
                    if (income > 0 && expenses >= 0) {
                        val suggested = viewModel.calculateSuggestedSaving()
                        if (suggested > 0) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Success.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "اقتراح: ${String.format("%.0f", suggested)} $currency",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Success,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            "30% من دخلك المتاح",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                    
                                    TextButton(
                                        onClick = {
                                            viewModel.updateMonthlySaving(suggested.toString())
                                        }
                                    ) {
                                        Text("تطبيق")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Products Timeline Card
            if (productsWithProgress.isNotEmpty() && saving > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "الخط الزمني للمنتجات",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        productsWithProgress.take(5).forEach { productWithProgress ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        productWithProgress.product.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "${String.format("%.0f", productWithProgress.product.price)} $currency",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "${productWithProgress.monthsNeeded} شهر",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        productWithProgress.estimatedDate,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                            
                            if (productWithProgress != productsWithProgress.take(5).last()) {
                                Divider(color = Divider, thickness = 1.dp)
                            }
                        }

                        if (productsWithProgress.size > 5) {
                            Text(
                                text = "و ${productsWithProgress.size - 5} منتجات أخرى",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Save Button
            Button(
                onClick = {
                    viewModel.saveBudget {
                        showSuccessSnackbar = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = viewModel.isFormValid() && !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ الميزانية", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun BudgetScreenPreview() {
    WishListSmartTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "إدارة الميزانية",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.AccountBalance, null, tint = Primary)
                }
                
                // Income Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("الدخل الشهري", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = "10000",
                            onValueChange = {},
                            label = { Text("الدخل الشهري (ر.س)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                
                // Expenses Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("المصروفات الثابتة", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = "5000",
                            onValueChange = {},
                            label = { Text("المصروفات الشهرية (ر.س)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                
                // Savings Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("الادخار الشهري", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = "1500",
                            onValueChange = {},
                            label = { Text("الادخار الشهري (ر.س)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        
                        // Progress Indicator
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("نسبة الادخار: 30%", color = Success, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                
                // Save Button
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ الميزانية")
                }
            }
        }
    }
}

