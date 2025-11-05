# دليل معاينة تصميم الشاشات بدون محاكي

## 📱 كيفية معاينة التصميم في Android Studio

### ✅ الطريقة الأولى: استخدام Preview Panel

#### الخطوات:

1. **افتح Android Studio**

2. **افتح أي ملف شاشة** من:
   ```
   app/src/main/java/com/example/app3/ui/screens/
   ├── AddProductScreen.kt          ✅ يحتوي على @Preview
   ├── BudgetScreen.kt              ✅ يحتوي على @Preview
   ├── WishListScreenEnhanced.kt
   ├── OffersScreen.kt
   └── WishListScreen.kt
   ```

3. **انتظر بناء المشروع** (Gradle Sync)

4. **اعرض لوحة المعاينة**:
   - اضغط على **Split** أو **Design** في الزاوية اليمنى العلوية
   - أو اختر: `View` → `Tool Windows` → `Preview`

5. **شاهد التصميم مباشرة!**
   - ستظهر لك معاينة حية للشاشة
   - يمكنك التفاعل مع العناصر (في Interactive Mode)

### 🎨 مميزات Preview Panel:

- ✅ **معاينة فورية** بدون تشغيل التطبيق
- ✅ **معاينات متعددة** (فاتح/داكن، لغات مختلفة، أحجام شاشات)
- ✅ **Interactive Mode** للتفاعل مع UI
- ✅ **تحديث تلقائي** عند تعديل الكود

---

## 🖼️ الملفات التي تحتوي على Preview

### 1. AddProductScreen.kt
```kotlin
@Preview(showBackground = true, locale = "ar")
@Composable
fun AddProductScreenPreview()
```
**يعرض**: نموذج إضافة منتج جديد

### 2. BudgetScreen.kt
```kotlin
@Preview(showBackground = true, locale = "ar")
@Composable
fun BudgetScreenPreview()
```
**يعرض**: شاشة إدارة الميزانية

---

## 📸 كيفية أخذ لقطات شاشة

### من Preview Panel:

1. افتح الـ Preview
2. اضغط بالزر الأيمن على المعاينة
3. اختر **"Copy Image"**
4. الصق الصورة في أي برنامج

### من خلال Android Studio:

1. في لوحة Preview
2. ابحث عن أيقونة الكاميرا 📷
3. اضغط عليها لحفظ لقطة الشاشة

---

## 🎯 معاينة بأحجام شاشات مختلفة

يمكنك إضافة معاينات متعددة بأحجام مختلفة:

```kotlin
@Preview(
    name = "Phone",
    device = Devices.PHONE,
    showBackground = true
)
@Preview(
    name = "Tablet",
    device = Devices.TABLET,
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MyScreenPreview() {
    WishListSmartTheme {
        // المحتوى هنا
    }
}
```

---

## 🌙 معاينة الوضع الليلي

```kotlin
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MyPreview() {
    WishListSmartTheme {
        // المحتوى
    }
}
```

---

## 🔄 Interactive Preview Mode

### كيفية التفعيل:

1. في لوحة Preview
2. اضغط على أيقونة **Interactive** 🎮
3. الآن يمكنك:
   - الضغط على الأزرار
   - الكتابة في حقول النص
   - التمرير (Scroll)
   - فتح القوائم

---

## 🚀 نصائح لمعاينة أسرع

### 1. **Live Edit Mode**
```kotlin
// في Android Studio Electric Eel وما بعد
// سيتم تحديث المعاينة تلقائياً أثناء الكتابة
```

### 2. **تشغيل Auto Build**
- `Settings` → `Build, Execution, Deployment` → `Compiler`
- فعّل `Build project automatically`

### 3. **استخدام PreviewParameter**
```kotlin
@Preview
@Composable
fun ProductCardPreview(
    @PreviewParameter(ProductProvider::class) product: Product
) {
    ProductCard(product = product)
}

class ProductProvider : PreviewParameterProvider<Product> {
    override val values = sequenceOf(
        Product("آيفون 15", 5000.0),
        Product("MacBook Pro", 12000.0)
    )
}
```

---

## ❌ إذا لم تظهر المعاينة

### الحلول:

1. **Build المشروع**:
   ```
   Build → Rebuild Project
   ```

2. **مسح Cache**:
   ```
   File → Invalidate Caches / Restart
   ```

3. **تحديث Gradle**:
   ```
   ./gradlew clean build
   ```

4. **تأكد من وجود**:
   ```kotlin
   import androidx.compose.ui.tooling.preview.Preview
   ```

---

## 📊 مقارنة الطرق

| الطريقة | السرعة | التفاعل | الدقة |
|---------|--------|---------|-------|
| Preview Panel | ⚡⚡⚡ سريع جداً | ✅ نعم | ✅ دقيق 100% |
| المحاكي | 🐌 بطيء | ✅ نعم | ✅ دقيق 100% |
| جهاز حقيقي | ⚡ سريع | ✅ نعم | ✅ دقيق 100% |

---

## 🎨 أمثلة Preview جاهزة

### مثال 1: بطاقة منتج
```kotlin
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    WishListSmartTheme {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("آيفون 15 برو", style = MaterialTheme.typography.titleLarge)
                Text("5000 ر.س", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
```

### مثال 2: قائمة منتجات
```kotlin
@Preview(showBackground = true, heightDp = 600)
@Composable
fun ProductListPreview() {
    WishListSmartTheme {
        LazyColumn {
            items(5) { index ->
                Text("منتج ${index + 1}", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
```

---

## 🎯 الخلاصة

**أفضل طريقة لمعاينة التصميم**:
1. استخدم `@Preview` في Android Studio
2. فعّل Interactive Mode للتفاعل
3. استخدم معاينات متعددة (فاتح/داكن، أحجام مختلفة)

**لا حاجة للمحاكي** للمعاينة السريعة! 🚀

---

## 📞 تحتاج مساعدة؟

إذا واجهت مشاكل:
1. تأكد من تحديث Android Studio
2. تأكد من Gradle Sync مكتمل
3. جرب Rebuild Project
4. تأكد من وجود `@Preview` في الكود

