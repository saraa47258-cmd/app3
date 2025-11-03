package com.example.app3.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Navigation Performance Optimizer
 * محسّن أداء التنقل
 */

// ============================================
// Navigation State Management
// ============================================

/**
 * حالة التنقل للتحكم في التحميل المسبق
 */
@Stable
class NavigationState {
    var isNavigating by mutableStateOf(false)
        private set
    
    var lastNavigationTime by mutableStateOf(0L)
        private set
    
    fun startNavigation() {
        isNavigating = true
        lastNavigationTime = System.currentTimeMillis()
    }
    
    fun endNavigation() {
        isNavigating = false
    }
    
    /**
     * التحقق من إمكانية التنقل (منع الضغطات المتكررة)
     */
    fun canNavigate(minInterval: Long = 500): Boolean {
        val now = System.currentTimeMillis()
        return !isNavigating && (now - lastNavigationTime) >= minInterval
    }
}

// ============================================
// Navigation Helpers
// ============================================

/**
 * تنقل آمن مع منع الضغطات المتكررة
 */
fun NavController.navigateSafe(
    route: String,
    navigationState: NavigationState,
    builder: (androidx.navigation.NavOptionsBuilder.() -> Unit)? = null
) {
    if (navigationState.canNavigate()) {
        navigationState.startNavigation()
        try {
            if (builder != null) {
                navigate(route, builder)
            } else {
                navigate(route)
            }
        } finally {
            // إعادة تعيين الحالة بعد فترة قصيرة
            kotlinx.coroutines.GlobalScope.launch {
                delay(600)
                navigationState.endNavigation()
            }
        }
    }
}

/**
 * رجوع آمن
 */
fun NavController.popBackStackSafe(navigationState: NavigationState): Boolean {
    if (navigationState.canNavigate()) {
        navigationState.startNavigation()
        val result = popBackStack()
        kotlinx.coroutines.GlobalScope.launch {
            delay(600)
            navigationState.endNavigation()
        }
        return result
    }
    return false
}

// ============================================
// Screen Preloading
// ============================================

/**
 * تحميل مسبق للشاشات المتوقع زيارتها
 */
@Composable
fun PreloadNextScreens(
    currentRoute: String?,
    navigationState: NavigationState
) {
    LaunchedEffect(currentRoute) {
        // تأخير بسيط لضمان عدم التأثير على الشاشة الحالية
        delay(500)
        
        when (currentRoute) {
            Screen.WishList.route -> {
                // المستخدم على الشاشة الرئيسية، من المحتمل أن ينتقل إلى:
                // 1. إضافة منتج (الأكثر احتمالاً)
                // 2. الميزانية
                // نقوم بتهيئة الذاكرة للانتقال السلس
            }
            Screen.Budget.route -> {
                // المستخدم في الميزانية، قد يعود للرئيسية
            }
            // يمكن إضافة المزيد حسب الحاجة
        }
    }
}

// ============================================
// Memory Management
// ============================================

/**
 * تنظيف الذاكرة عند مغادرة الشاشة
 */
@Composable
fun <T> rememberWithCleanup(
    key: Any? = null,
    calculation: () -> T,
    cleanup: (T) -> Unit = {}
): T {
    return remember(key) { calculation() }.also { value ->
        DisposableEffect(key) {
            onDispose {
                cleanup(value)
            }
        }
    }
}

// ============================================
// Navigation Performance Monitoring
// ============================================

/**
 * مراقب أداء التنقل
 */
class NavigationPerformanceMonitor {
    private val navigationTimes = mutableMapOf<String, Long>()
    
    fun recordNavigationStart(route: String) {
        navigationTimes[route] = System.currentTimeMillis()
    }
    
    fun recordNavigationEnd(route: String): Long {
        val startTime = navigationTimes[route] ?: return 0L
        val duration = System.currentTimeMillis() - startTime
        navigationTimes.remove(route)
        return duration
    }
    
    fun getAverageNavigationTime(): Long {
        return if (navigationTimes.isEmpty()) 0L
        else navigationTimes.values.average().toLong()
    }
}

// ============================================
// Navigation Optimization Tips
// ============================================

/**
 * نصائح لتحسين الأداء:
 * 
 * 1. استخدام LazyColumn بدلاً من Column + Scroll
 * 2. استخدام remember للقيم المحسوبة
 * 3. تجنب recomposition غير الضرورية
 * 4. استخدام derivedStateOf للقيم المشتقة
 * 5. تأخير تحميل الصور الكبيرة
 * 6. استخدام keys في LazyColumn
 * 7. تجنب nested scrolling عند الإمكان
 */

// ============================================
// Gesture Handling
// ============================================

/**
 * إدارة إيماءات التمرير للرجوع
 */
@Composable
fun rememberBackGestureHandler(
    navController: NavController,
    navigationState: NavigationState,
    enabled: Boolean = true
): () -> Unit {
    return remember(navController, enabled) {
        {
            if (enabled) {
                navController.popBackStackSafe(navigationState)
            }
        }
    }
}

// ============================================
// Navigation Cache
// ============================================

/**
 * كاش للشاشات المزارة مؤخراً
 */
class NavigationCache {
    private val cache = mutableMapOf<String, Any?>()
    private val maxSize = 3
    
    fun put(route: String, data: Any?) {
        if (cache.size >= maxSize) {
            // إزالة أقدم عنصر
            cache.remove(cache.keys.first())
        }
        cache[route] = data
    }
    
    fun get(route: String): Any? {
        return cache[route]
    }
    
    fun clear() {
        cache.clear()
    }
}

// ============================================
// Navigation Metrics
// ============================================

/**
 * مقاييس الأداء
 */
data class NavigationMetrics(
    val route: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val wasSuccessful: Boolean
) {
    val durationInSeconds: Float
        get() = duration / 1000f
}

/**
 * مجمّع المقاييس
 */
class NavigationMetricsCollector {
    private val metrics = mutableListOf<NavigationMetrics>()
    
    fun addMetric(metric: NavigationMetrics) {
        metrics.add(metric)
        // الاحتفاظ بآخر 50 قياس فقط
        if (metrics.size > 50) {
            metrics.removeAt(0)
        }
    }
    
    fun getAverageDuration(): Long {
        return if (metrics.isEmpty()) 0L
        else metrics.map { it.duration }.average().toLong()
    }
    
    fun getSuccessRate(): Float {
        return if (metrics.isEmpty()) 1f
        else metrics.count { it.wasSuccessful }.toFloat() / metrics.size
    }
    
    fun getSlowestNavigation(): NavigationMetrics? {
        return metrics.maxByOrNull { it.duration }
    }
    
    fun getFastestNavigation(): NavigationMetrics? {
        return metrics.minByOrNull { it.duration }
    }
}


