package com.example.app3.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.unit.IntOffset

/**
 * Navigation Animations System
 * نظام حركات احترافي للتنقل بين الصفحات
 */

// ============================================
// Slide Animations (انزلاق)
// ============================================

/**
 * Slide من اليمين - للصفحات الجديدة
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideInFromRight() = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

/**
 * Slide لليسار - للخروج من الصفحة الحالية
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideOutToLeft() = slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth / 3 },
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

/**
 * Slide من اليسار - للرجوع
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideInFromLeft() = slideInHorizontally(
    initialOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

/**
 * Slide لليمين - للخروج عند الرجوع
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideOutToRight() = slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

// ============================================
// Fade Animations (تلاشي)
// ============================================

/**
 * Fade In - للظهور
 */
fun fadeIn() = fadeIn(
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing
    )
)

/**
 * Fade Out - للاختفاء
 */
fun fadeOut() = fadeOut(
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing
    )
)

// ============================================
// Scale Animations (تكبير/تصغير)
// ============================================

/**
 * Scale Up - تكبير عند الظهور
 */
@OptIn(ExperimentalAnimationApi::class)
fun scaleIn() = scaleIn(
    initialScale = 0.9f,
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

/**
 * Scale Down - تصغير عند الاختفاء
 */
@OptIn(ExperimentalAnimationApi::class)
fun scaleOut() = scaleOut(
    targetScale = 0.9f,
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
)

// ============================================
// Vertical Slide (انزلاق عمودي)
// ============================================

/**
 * Slide من الأسفل - للـ Modal Screens
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideInFromBottom() = slideInVertically(
    initialOffsetY = { fullHeight -> fullHeight },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)

/**
 * Slide للأسفل - للخروج من Modal
 */
@OptIn(ExperimentalAnimationApi::class)
fun slideOutToBottom() = slideOutVertically(
    targetOffsetY = { fullHeight -> fullHeight },
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)

// ============================================
// Shared Element Style (للانتقالات المشتركة)
// ============================================

/**
 * Cross Fade - للانتقال السلس بين التبويبات
 */
fun crossFadeIn() = fadeIn(
    animationSpec = tween(
        durationMillis = 300,
        delayMillis = 150,
        easing = LinearEasing
    )
)

fun crossFadeOut() = fadeOut(
    animationSpec = tween(
        durationMillis = 150,
        easing = LinearEasing
    )
)

// ============================================
// Custom Combined Animations
// ============================================

/**
 * Elegant Enter - دخول أنيق (Scale + Slide + Fade)
 */
@OptIn(ExperimentalAnimationApi::class)
fun elegantEnter() = slideInVertically(
    initialOffsetY = { fullHeight -> fullHeight / 4 },
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
    )
) + scaleIn(
    initialScale = 0.95f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
)

/**
 * Elegant Exit - خروج أنيق
 */
@OptIn(ExperimentalAnimationApi::class)
fun elegantExit() = slideOutVertically(
    targetOffsetY = { fullHeight -> -fullHeight / 4 },
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
) + scaleOut(
    targetScale = 0.95f,
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)

// ============================================
// Navigation Transition Specs
// ============================================

/**
 * Default Forward Navigation
 * الانتقال الافتراضي للأمام
 */
@OptIn(ExperimentalAnimationApi::class)
fun defaultForwardTransition(): EnterTransition {
    return slideInFromRight()
}

@OptIn(ExperimentalAnimationApi::class)
fun defaultForwardExitTransition(): ExitTransition {
    return slideOutToLeft()
}

/**
 * Default Backward Navigation
 * الانتقال الافتراضي للخلف
 */
@OptIn(ExperimentalAnimationApi::class)
fun defaultBackwardTransition(): EnterTransition {
    return slideInFromLeft()
}

@OptIn(ExperimentalAnimationApi::class)
fun defaultBackwardExitTransition(): ExitTransition {
    return slideOutToRight()
}

/**
 * Modal Navigation (مثل شاشة إضافة منتج)
 */
@OptIn(ExperimentalAnimationApi::class)
fun modalEnterTransition(): EnterTransition {
    return slideInFromBottom()
}

@OptIn(ExperimentalAnimationApi::class)
fun modalExitTransition(): ExitTransition {
    return slideOutToBottom()
}

/**
 * Tab Navigation (للتبويبات السفلية)
 */
fun tabEnterTransition(): EnterTransition {
    return crossFadeIn()
}

fun tabExitTransition(): ExitTransition {
    return crossFadeOut()
}

// ============================================
// Navigation Performance Utils
// ============================================

/**
 * تأخير بسيط لضمان سلاسة الحركة
 */
fun getOptimalDelay(isComplexScreen: Boolean): Int {
    return if (isComplexScreen) 50 else 0
}

/**
 * مدة مثالية حسب نوع الانتقال
 */
fun getOptimalDuration(transitionType: TransitionType): Int {
    return when (transitionType) {
        TransitionType.FAST -> 250
        TransitionType.NORMAL -> 400
        TransitionType.SMOOTH -> 600
        TransitionType.TAB -> 300
    }
}

enum class TransitionType {
    FAST,      // سريع
    NORMAL,    // عادي
    SMOOTH,    // ناعم
    TAB        // تبويبات
}


