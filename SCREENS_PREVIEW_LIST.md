# قائمة معاينات الشاشات (Preview List)

## 📱 جميع الشاشات التي تحتوي على Preview

### ✅ 1. AddProductScreen
**المسار**: `app/src/main/java/com/example/app3/ui/screens/AddProductScreen.kt`

**دالة Preview**:
```kotlin
@Preview(showBackground = true, locale = "ar")
@Composable
fun AddProductScreenPreview()
```

**المحتوى**:
- نموذج إضافة منتج جديد
- حقول الإدخال (اسم، سعر، فئة)
- اختيار الأولوية
- صورة المنتج

**كيفية المعاينة**:
1. افتح الملف `AddProductScreen.kt`
2. اضغط على **Split** أو **Design**
3. ابحث عن `AddProductScreenPreview` في لوحة Preview

---

### ✅ 2. BudgetScreen
**المسار**: `app/src/main/java/com/example/app3/ui/screens/BudgetScreen.kt`

**دالة Preview**:
```kotlin
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetScreenPreview()
```

**المحتوى**:
- نموذج إدارة الميزانية
- حقل الدخل الشهري (10000 ر.س)
- حقل المصروفات (5000 ر.س)
- حقل الادخار (1500 ر.س)
- نسبة الادخار: 30%

**كيفية المعاينة**:
1. افتح الملف `BudgetScreen.kt`
2. اضغط على **Split** أو **Design**
3. ستظهر معاينة شاشة الميزانية كاملة

---

### ✅ 3. OffersScreen
**المسار**: `app/src/main/java/com/example/app3/ui/screens/OffersScreen.kt`

**دالة Preview**:
```kotlin
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OffersScreenPreview()
```

**المحتوى**:
- 3 عروض تجريبية:
  1. آيفون 15 برو (خصم 15%)
  2. سماعات AirPods Pro (خصم 20%)
  3. ساعة Apple Watch (خصم 10%)
- السعر الأصلي والسعر بعد الخصم
- مدة انتهاء العرض
- زر فتح العرض

**كيفية المعاينة**:
1. افتح الملف `OffersScreen.kt`
2. اضغط على **Split** أو **Design**
3. ستظهر قائمة العروض

---

### ✅ 4. WishListScreen (ProductCard)
**المسار**: `app/src/main/java/com/example/app3/ui/screens/WishListScreen.kt`

**دالة Preview**:
```kotlin
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCardPreview()
```

**المحتوى**:
- بطاقة منتج واحد (آيفون 15 برو)
- السعر: 5000 ر.س
- الأولوية: عالية
- الوقت المتوقع: 5 أشهر
- شريط التقدم: 40%
- التاريخ المتوقع: مايو 2025

**كيفية المعاينة**:
1. افتح الملف `WishListScreen.kt`
2. اضغط على **Split** أو **Design**
3. ستظهر بطاقة المنتج

---

### ⏳ 5. WishListScreenEnhanced
**المسار**: `app/src/main/java/com/example/app3/ui/screens/WishListScreenEnhanced.kt`

**الحالة**: ⚠️ لا يحتوي على Preview بعد (شاشة معقدة تحتاج ViewModel)

**يمكن إضافة Preview مبسط لاحقاً**

---

## 🎨 كيفية استخدام Preview في Android Studio

### الخطوات:

#### 1. **افتح ملف الشاشة**
```
app/src/main/java/com/example/app3/ui/screens/
├── AddProductScreen.kt      ✅
├── BudgetScreen.kt          ✅
├── OffersScreen.kt          ✅
├── WishListScreen.kt        ✅
└── WishListScreenEnhanced.kt ⏳
```

#### 2. **فعّل Preview Panel**
- انقر على **Split** في أعلى يمين المحرر
- أو: `View` → `Tool Windows` → `Preview`

#### 3. **انتظر البناء**
- أول مرة قد يستغرق دقيقة
- سيظهر "Building..." ثم "Rendering..."

#### 4. **شاهد المعاينة!** 🎉

---

## 🔍 مميزات Preview

### ✅ معاينة فورية
- لا حاجة لتشغيل المحاكي
- تحديث تلقائي عند تعديل الكود

### ✅ Interactive Mode
1. اضغط على أيقونة **Interactive** 🎮
2. تفاعل مع الأزرار والحقول
3. اختبر التصميم بدون تشغيل التطبيق

### ✅ معاينات متعددة
```kotlin
@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyPreview() { ... }
```

### ✅ أحجام مختلفة
```kotlin
@Preview(name = "Phone", device = Devices.PHONE)
@Preview(name = "Tablet", device = Devices.TABLET)
@Composable
fun MyPreview() { ... }
```

---

## 📊 جدول ملخص

| الشاشة | الملف | دالة Preview | الحالة |
|--------|------|--------------|--------|
| إضافة منتج | `AddProductScreen.kt` | `AddProductScreenPreview()` | ✅ جاهز |
| الميزانية | `BudgetScreen.kt` | `BudgetScreenPreview()` | ✅ جاهز |
| العروض | `OffersScreen.kt` | `OffersScreenPreview()` | ✅ جاهز |
| بطاقة منتج | `WishListScreen.kt` | `ProductCardPreview()` | ✅ جاهز |
| القائمة المحسّنة | `WishListScreenEnhanced.kt` | - | ⏳ قريباً |

---

## 🚀 نصائح سريعة

### 1. **تحديث سريع**
اضغط على **Refresh** في لوحة Preview لإعادة البناء

### 2. **حل مشكلة "Render Issue"**
```
Build → Clean Project
Build → Rebuild Project
```

### 3. **زيادة سرعة Preview**
في `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2048m
```

### 4. **معاينة مع بيانات حقيقية**
```kotlin
@Preview
@Composable
fun PreviewWithData() {
    val sampleProduct = Product(...)
    ProductCard(product = sampleProduct)
}
```

---

## 🎯 الخلاصة

### ✅ لديك الآن 4 شاشات جاهزة للمعاينة:

1. **AddProductScreen** - نموذج إضافة منتج
2. **BudgetScreen** - إدارة الميزانية
3. **OffersScreen** - قائمة العروض
4. **WishListScreen** - بطاقة منتج

### 📍 كيف تعرضها:

1. افتح الملف في Android Studio
2. اضغط **Split** أو **Design**
3. شاهد التصميم مباشرة!

**لا تحتاج المحاكي أبداً!** 🎉

---

## 🔗 ملفات مرجعية

- **PREVIEW_GUIDE.md** - دليل شامل لاستخدام Preview
- **COMPOSE_VS_XML.md** - الفرق بين Compose و XML
- **README.md** - معلومات المشروع

---

## 💡 تذكير مهم

**Jetpack Compose لا يستخدم XML!**

- ❌ لا توجد ملفات `.xml` للشاشات
- ✅ كل شيء مكتوب بـ Kotlin
- ✅ استخدم `@Preview` للمعاينة

**هذا أفضل من XML بكثير!** 🚀

