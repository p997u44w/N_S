# Nexa — بک‌اند PHP + اپ فلاتر

## ساختار پروژه
```
school-app/
├── backend/                  ← بک‌اند PHP ماژولار (برای آپلود روی هاست)
│   ├── config/config.php     ← تنظیمات دیتابیس، JWT، AI
│   ├── core/                 ← کلاس‌های پایه (DB, Auth, JWT, Response, FileHelper)
│   ├── modules/
│   │   ├── auth/             ← ثبت‌نام مدیر (با تایید AI) + ورود مشترک همه نقش‌ها
│   │   ├── admin/             ← افزودن مدرسه، تم هر مدرسه، محدودیت پیام آزاد
│   │   ├── manager/           ← کلاس، دانش‌آموز، معلم، اطلاعیه
│   │   ├── teacher/           ← کلاس‌های من، تکلیف/ویدیو
│   │   ├── student/            ← معلم‌های من، تکالیف، ارسال تکلیف
│   │   ├── messages/           ← گفتگوی معلم-دانش‌آموز
│   │   └── ai/SchoolVerifier.php  ← اعتبارسنجی نام/کد ملی/شهر مدرسه
│   ├── uploads/               ← فایل‌های آپلودی، به تفکیک مدرسه/کلاس
│   ├── database/schema.sql
│   ├── create_admin.php       ← فقط یک‌بار برای ساخت اولین ادمین اجرا بشه
│   └── DEPLOY.md              ← راهنمای نصب کامل
└── frontend/                  ← اپ فلاتر Nexa
    ├── assets/
    │   ├── logo/nexa_logo.png          ← لوگوی اصلی (پس‌زمینه شفاف، برای داخل اپ)
    │   └── icon/app_icon*.png          ← فایل‌های ساخت آیکون اپ (اندروید/iOS)
    └── lib/
        ├── main.dart                    ← تم تیره/شیشه‌ای + بارگذاری تنظیمات
        ├── theme/nexa_tokens.dart       ← رنگ‌های پایه‌ی برند Nexa
        ├── services/
        │   ├── api_service.dart         ← ارتباط با بک‌اند + تم هر مدرسه
        │   ├── settings_service.dart    ← ذخیره‌ی روشن/خاموش بودن جلوه‌ها
        │   └── online_class_service.dart← اتصال سوکت + WebRTC + تخته + دست‌بالا
        ├── widgets/
        │   ├── animated_mesh_background.dart  ← پس‌زمینه‌ی امضادار (mesh gradient + ذرات نور)
        │   ├── glass_card.dart               ← کارت شیشه‌ای (glassmorphism)
        │   ├── animated_glass_field.dart      ← فیلد ورودی با انیمیشن فوکوس
        │   ├── animated_primary_button.dart   ← دکمه‌ی اصلی با haptic + لودینگ
        │   ├── effects_toggle.dart            ← دکمه‌ی روشن/خاموش کردن جلوه‌ها
        │   ├── whiteboard_canvas.dart          ← تخته‌ی مشترک زنده‌ی کلاس آنلاین
        │   └── dashboard_scaffold.dart         ← اسکلت مشترک داشبوردها
        └── screens/           ← لاگین، ثبت‌نام مدیر، داشبورد هر نقش، کلاس آنلاین

realtime-server/               ← سرور سیگنالینگ کلاس آنلاین (Node.js، برای VPS)
├── src/server.js              ← Socket.IO: WebRTC signaling + تخته + دست‌بالا
├── package.json
├── ecosystem.config.js        ← اجرای پایدار با PM2
├── .env.example
└── DEPLOY.md                  ← راهنمای کامل نصب روی VPS (Nginx + SSL + TURN)

hub-backend/                   ← هاب مرکزی سیستم چندهاستی (اختیاری، PHP)
├── modules/schools/resolve.php   ← اپ اول از اینجا می‌پرسه «کد مدرسه مال کدوم هاسته»
├── modules/schools/create.php    ← ساخت مدرسه‌ی جدید روی یکی از هاست‌ها
├── modules/hosts/                ← ثبت هاست‌های مختلف
├── database/schema.sql
└── DEPLOY.md                     ← راهنمای کامل (نحوه‌ی وصل کردن چند هاست)
```

## هویت بصری Nexa
- **پالت رنگ پایه**: پس‌زمینه‌ی تیره `#0A0E17`، حباب‌های نور نیلی/فیروزه‌ای (از تم هر مدرسه میان) + کهربایی `#FFB020` برای تاکید.
- **فونت**: Space Grotesk برای لوگوی «Nexa»، Vazirmatn (از Google Fonts) برای متن فارسی.
- **پس‌زمینه‌ی امضادار**: `AnimatedMeshBackground` — چند حباب رنگی که به‌آرومی می‌چرخن و بلور می‌شن + ذرات نور شناور، کاملا native فلاتر (بدون شیدر یا پکیج اضافه).
- **جلوه‌ها قابل خاموش‌شدنه**: دکمه‌ی کوچیک بالای صفحه (`EffectsToggle`) با `SettingsService` ذخیره می‌شه. وقتی خاموش باشه، هیچ `AnimationController` ای اجرا نمی‌شه و ذرات کشیده نمی‌شن — مناسب گوشی‌های ضعیف‌تر یا صرفه‌جویی باتری.
- **آیکون اپ**: از همون لوگو ساخته شده. برای تولید آیکون‌های واقعی اندروید/iOS:
  ```
  cd frontend
  flutter pub get
  flutter pub run flutter_launcher_icons
  ```

> این نسخه فعلا فقط حوزه‌ی مدرسه رو پوشش می‌ده، ولی معماری ماژولار بک‌اند (هر نقش/حوزه یه پوشه‌ی جدا زیر `modules/`) و سیستم طراحی مشترک فرانت (توکن‌ها + ویجت‌های قابل استفاده مجدد) طوری هست که بعداً بشه حوزه‌های دیگه رو کنارش اضافه کرد.

## قوانین دسترسی پیاده‌سازی‌شده
- هر **مدیر** فقط کلاس/دانش‌آموز/معلمِ **مدرسه‌ی خودش** رو می‌بینه (فیلتر با `school_id` در همه‌ی کوئری‌ها).
- هر **معلم** فقط کلاس‌هایی که مدیر بهش تخصیص داده رو می‌بینه (جدول `class_teacher`)، مرتب از پایه‌ی پایین به بالا (`grade_level ASC`).
- هر **دانش‌آموز** فقط معلم‌های همون کلاس خودش رو می‌بینه.
- بعد از ارسال تکلیف، یک گفتگو (`message_threads`) بین دانش‌آموز و معلم باز می‌شه. دانش‌آموز به تعداد `free_message_limit` مدرسه‌ش (پیش‌فرض ۱، قابل تغییر توسط ادمین) می‌تونه بدون اجازه پیام بده؛ بعدش باید معلم با `messages/allow` اجازه‌ی ادامه بده.
- همه‌ی فایل‌های آپلودی (تکلیف، عکس، ویس) به تفکیک `school_id` و `class_id` پوشه‌بندی می‌شن و نام فایل شامل نام فرستنده و گیرنده‌ست، مثلا:
  `uploads/submissions/school_2/class_5/علی-رضایی_to_خانم-احمدی_20260820_143210_ab12c.jpg`

## خلاصه‌ی Endpoint ها

| Method | مسیر | نقش | توضیح |
|---|---|---|---|
| POST | `auth/register-manager` | عمومی | ثبت‌نام مدیر + مدرسه (با تایید AI) |
| POST | `auth/login` | عمومی | ورود همه‌ی نقش‌ها |
| POST | `admin/add-school` | admin | افزودن مدرسه |
| GET  | `admin/list-schools` | admin | لیست مدرسه‌ها |
| POST | `admin/set-school-theme` | admin | تعیین رنگ/لوگوی هر مدرسه (multipart) |
| POST | `admin/set-school-settings` | admin | تغییر `free_message_limit` هر مدرسه |
| GET  | `school/get-theme` | همه | گرفتن تم و محدودیت پیام مدرسه‌ی خودت |
| POST | `manager/add-class` | manager | افزودن کلاس |
| GET  | `manager/list-classes` | manager | لیست کلاس‌ها (مرتب بر اساس مقطع) |
| POST | `manager/add-student` | manager | افزودن دانش‌آموز به یک کلاس |
| GET  | `manager/list-students` | manager | لیست دانش‌آموزان (با فیلتر اختیاری `class_id`) |
| POST | `manager/add-teacher` | manager | افزودن معلم |
| GET  | `manager/list-teachers` | manager | لیست معلم‌ها + کلاس‌های هرکدوم |
| POST | `manager/assign-teacher` | manager | تخصیص معلم به کلاس |
| POST | `manager/add-announcement` | manager | ثبت اطلاعیه (کل مدرسه یا یک کلاس) |
| GET  | `teacher/list-classes` | teacher | کلاس‌های تخصیص‌داده‌شده، مرتب پایین به بالا |
| GET  | `teacher/list-students` | teacher | دانش‌آموزان یک کلاس (`class_id`) |
| POST | `teacher/add-assignment` | teacher | تکلیف/ویدیو جدید (multipart) |
| GET  | `teacher/list-assignments` | teacher | تکالیف یک کلاس |
| GET  | `student/list-teachers` | student | معلم‌های کلاس خودش |
| GET  | `student/list-assignments` | student | تکالیف کلاس خودش |
| POST | `student/submit-assignment` | student | ارسال تکلیف (multipart، متن/عکس/ویس) |
| POST | `messages/send` | teacher, student | ارسال پیام در یک گفتگو (multipart) |
| GET  | `messages/list` | teacher, student, manager | پیام‌های یک گفتگو (مدیر فقط نظارتی) |
| GET  | `messages/list-threads` | teacher, student, manager | لیست گفتگوها (مدیر = همه‌ی گفتگوهای مدرسه‌ش) |
| POST | `messages/allow` | teacher | اجازه‌ی ادامه‌ی گفتگو به دانش‌آموز |

### قابلیت‌های جدید

| Method | مسیر | نقش | توضیح |
|---|---|---|---|
| POST | `auth/change-password` | همه | تغییر رمز عبور خود کاربر |
| POST | `admin/suspend-school` | admin | تعلیق مدرسه (اطلاعات می‌مونه، هیچ‌کس نمی‌تونه کاری کنه) |
| POST | `admin/unsuspend-school` | admin | خروج از تعلیق |
| POST | `admin/delete-school` | admin | حذف کامل و برگشت‌ناپذیر مدرسه |
| POST | `admin/set-school-feature` | admin | روشن/خاموش کردن یک قابلیت خاص برای یک مدرسه |
| GET  | `admin/list-school-features` | admin | وضعیت همه‌ی قابلیت‌ها برای یک مدرسه |
| POST | `admin/add-student` / `admin/add-teacher` | admin | افزودن مستقیم دانش‌آموز/معلم به هر مدرسه‌ای |
| POST | `admin/reset-user-password` | admin | ریست رمز هر کاربری |
| POST | `manager/reset-user-password` | manager | ریست رمز معلم/دانش‌آموز مدرسه‌ی خودش |
| POST | `teacher/grade-submission` | teacher | نمره‌دهی به تکلیف + درخواست ارسال مجدد |
| GET  | `class/roster` | teacher, manager | لیست دانش‌آموزان کلاس با چراغ وضعیت (قرمز/زرد) و امتیاز |
| POST | `points/add` | teacher, manager | ثبت امتیاز مثبت/منفی برای یک دانش‌آموز |
| POST | `exam/create` | teacher | ساخت امتحان برای یک کلاس |
| POST | `exam/add-question` | teacher | افزودن سوال (چهارگزینه‌ای یا تشریحی) |
| GET  | `exam/list` | teacher, student | لیست امتحانات یک کلاس |
| GET  | `exam/get` | teacher, student | جزئیات امتحان (پاسخ درست فقط برای معلم) |
| POST | `exam/submit` | student | ارسال پاسخ‌ها (چهارگزینه‌ای خودکار تصحیح می‌شه) |
| GET  | `exam/submissions` | teacher | لیست تحویل‌های یک امتحان + نمرات |
| POST | `exam/grade-essay` | teacher | نمره‌دهی دستی به پاسخ تشریحی |

### کلاس آنلاین

| Method | مسیر | نقش | توضیح |
|---|---|---|---|
| POST | `onlineclass/create` | manager, admin | ساخت جلسه‌ی کلاس آنلاین برای یک کلاس + تعیین معلم اصلی |
| GET  | `onlineclass/list` | همه | لیست جلسات یک کلاس |
| GET  | `onlineclass/verify-access` | همه | (برای سرور Node) تایید دسترسی کاربر به یک جلسه |
| POST | `onlineclass/end` | teacher, manager, admin | پایان‌دادن به جلسه |


همه‌ی endpoint های محافظت‌شده به‌جز `auth/login` و `auth/register-manager` نیاز به هدر `Authorization: Bearer <token>` دارن.

### قابلیت‌های روشن/خاموش‌شدنی به تفکیک مدرسه
با `admin/set-school-feature` (پارامترهای `school_id`, `feature_key`, `enabled`) این ۴ تا رو می‌شه جدا جدا برای هر مدرسه کنترل کرد:
- `exams` — امتحان
- `late_penalty` — کسر نمره‌ی تاخیر تکلیف
- `resubmit` — امکان درخواست ارسال مجدد تکلیف
- `points` — امتیاز مثبت/منفی

پیش‌فرض همه روشنه؛ اگه ردیفی برای یه مدرسه ثبت نشده باشه یعنی فعاله.

### ورود بر اساس Username
دیگه لاگین با شماره موبایل نیست؛ هر کاربر یه `username` یکتا داره که ادمین (برای مدیر/معلم/دانش‌آموز) یا مدیر (برای معلم/دانش‌آموز مدرسه‌ی خودش) موقع ساخت حساب تعیین می‌کنه. شماره موبایل اختیاریه و فقط برای تماسه.

### تعلیق مدرسه
با `admin/suspend-school`، مدرسه به وضعیت `suspended` می‌ره؛ در این حالت **هیچ‌کس** (مدیر، معلم، دانش‌آموز) نمی‌تونه لاگین کنه یا کاری انجام بده — اطلاعات کامل حفظ می‌مونه. با `admin/unsuspend-school` برمی‌گرده به حالت عادی. `admin/delete-school` برگشت‌ناپذیره و همه‌چیز (کلاس، کاربر، تکلیف، پیام، امتحان و ...) رو برای همیشه پاک می‌کنه.

### کلاس آنلاین (ویدیو/صدا/تخته زنده)
این تنها بخشیه که PHP به‌تنهایی کافی نیست؛ یه سرور جدا (`realtime-server/`، Node.js) روی VPS لازمه که سیگنالینگ WebRTC رو انجام بده. جزئیات کامل استقرارش تو `realtime-server/DEPLOY.md` هست. نکات کلیدی:

- **دیتابیس نداره.** هر بار کسی می‌خواد وارد یه جلسه بشه، توکن JWT خودش رو (همونی که با اپ لاگین کرده) به `onlineclass/verify-access` روی همین بک‌اند PHP می‌فرسته و نتیجه رو می‌گیره — یعنی همیشه از هاست میاد، نه یه منبع جدا.
- هر جلسه (`online_classes`) متعلق به یک کلاسه؛ مدیر یا ادمین موقع ساختنش مشخص می‌کنه معلم اصلی (`main_teacher_id`) کیه.
- داخل room: **معلم اصلی و هر معلم دیگه‌ای که به همون کلاس تخصیص داره + مدیر + ادمین** نقش «میزبان/ناظر» دارن (می‌تونن دوربین/صدا/اشتراک صفحه باز کنن، تخته بکشن، به دانش‌آموز اجازه‌ی صحبت بدن یا میوتش کنن). دانش‌آموزها فقط می‌بینن/می‌شنون و با دکمه‌ی «دست‌بالا» درخواست صحبت می‌دن.
- تخته‌ی مشترک (`whiteboard_canvas.dart`) فقط دست میزبان/ناظرهاست؛ رنگش هم فقط اونا عوض می‌کنن.
- اشتراک‌گذاری صفحه با `getDisplayMedia` + `replaceTrack` روی همون اتصال ویدیوی موجوده (نیازی به اتصال جدید نیست).
- پیاده‌سازی فعلی **mesh** هست (هر شرکت‌کننده مستقیم به بقیه وصل می‌شه) که برای کلاس‌های معمولی (چند ده نفر با ویدیوی فقط معلم) خوب کار می‌کنه؛ برای کلاس‌های خیلی بزرگ بعدا باید به یه SFU (مثل mediasoup) مهاجرت کرد.
- برای وصل‌شدن پشت فایروال/NAT سخت‌گیر، یه TURN server (مثل coturn، توضیحش تو DEPLOY.md هست) پیشنهاد می‌شه؛ فعلا فقط STUN عمومی گوگل تنظیم شده.

### سیستم چندهاستی (هاب مرکزی + هاست‌های مستقل)
اگه می‌خواید هر مدرسه (یا گروهی از مدرسه‌ها) رو رو یه هاست جدا نگه دارید و
یه نقطه‌ی مرکزی مشخص کنه هر مدرسه کجاست، از `hub-backend/` استفاده کنید.
جزئیات کامل (چطور هاست اضافه کنید، چطور مدرسه بسازید، امنیت رمز مشترک) تو
`hub-backend/DEPLOY.md` هست. خلاصه‌ی مکانیزم:

1. هر مدرسه یه `school_code` یکتا داره (مثلا `NEXA-4821`).
2. اپ فلاتر اول از هاب می‌پرسه «این کد مال کدوم هاسته» (`schools/resolve`، بدون نیاز به لاگین) و آدرس هاست رو ذخیره می‌کنه.
3. از اونجا به بعد، اپ مستقیم با همون هاست حرف می‌زنه — هاب دیگه وسط نیست.
4. ساخت/تعلیق/حذف مدرسه از پنل هاب انجام می‌شه؛ هاب با یه رمز مشترک (`X-Hub-Secret`) به هاست مقصد وصل می‌شه و دستور رو اونجا اجرا می‌کنه (`backend/modules/hub/*.php`).

اگه فقط یه بک‌اند دارید و نیازی به این پیچیدگی نیست، کافیه تو فلاتر
`ApiService.useHub` رو `false` کنید — همه‌چیز دقیقا مثل قبل (بدون هاب) کار می‌کنه.

**دسترسی‌های لازم روی گوشی** (بعد از `flutter create .`، این‌ها رو اضافه کنید):
- اندروید (`android/app/src/main/AndroidManifest.xml`):
  ```xml
  <uses-permission android:name="android.permission.CAMERA"/>
  <uses-permission android:name="android.permission.RECORD_AUDIO"/>
  <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.BLUETOOTH" android:maxSdkVersion="30"/>
  ```
- iOS (`ios/Runner/Info.plist`):
  ```xml
  <key>NSCameraUsageDescription</key>
  <string>برای کلاس آنلاین به دوربین نیاز است</string>
  <key>NSMicrophoneUsageDescription</key>
  <string>برای کلاس آنلاین به میکروفون نیاز است</string>
  ```

## اجرای فلاتر
اگه اولین باره که این پروژه رو باز می‌کنید و پوشه‌های `android`/`ios` وجود ندارن، اول این دستور رو بزنید تا اسکلت پلتفرم‌ها ساخته بشه:
```
cd frontend
flutter create --project-name nexa .
```
بعدش:
```
flutter pub get
flutter run
```
قبلش آدرس `baseUrl` رو توی `lib/services/api_service.dart` با آدرس واقعی بک‌اندتون عوض کنید.

برای ساخت آیکون واقعی اپ (از روی لوگوی Nexa) روی اندروید/iOS:
```
flutter pub run flutter_launcher_icons
```

## انتشار APK بدون گوگل‌پلی (پخش دستی بین مدرسه‌ها)

چون از گوگل‌پلی رد نمی‌شه، دو تا نکته‌ی مهم هست:

**۱. امضا (signing).** حتما با یه keystore واقعی امضا کنید (نه کلید debug پیش‌فرض
فلاتر)، وگرنه نسخه‌های بعدی رو نمی‌تونید روی گوشی‌هایی که نسخه‌ی قبلی رو
دارن آپدیت کنید. مراحلش:
```bash
keytool -genkey -v -keystore nexa-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias nexa
```
بعد یه فایل `frontend/android/key.properties` بسازید:
```
storePassword=<رمز keystore>
keyPassword=<همون رمز>
keyAlias=nexa
storeFile=../nexa-release.jks
```
و `frontend/android/app/build.gradle` رو طبق `frontend/android_snippets/app_build_gradle_signing.txt` وصل کنید.
بعدش:
```
flutter build apk --release
```

**۲. اطلاع‌رسانی نسخه‌ی جدید.** چون آپدیت خودکار (مثل گوگل‌پلی) وجود نداره،
هر بار که یه APK جدید ساختید:
1. فایل APK رو آپلود کنید تو `backend/downloads/` (مثلا `nexa-latest.apk`).
2. `backend/app_version.json` رو ویرایش کنید:
   ```json
   {
     "latest_version_code": 2,
     "latest_version_name": "1.0.1",
     "apk_url": "https://yourdomain.com/downloads/nexa-latest.apk",
     "changelog": "رفع باگ پیام‌ها",
     "force_update": false
   }
   ```
   `latest_version_code` باید همیشه از قبلی بزرگ‌تر باشه و دقیقا برابر با
   عددی باشه که تو `frontend/pubspec.yaml` بعد از `+` گذاشتید (مثلا `version: 1.0.1+2` یعنی version_code=۲).
3. همین! اپ خودش موقع باز شدن (صفحه‌ی لاگین) چک می‌کنه و اگه نسخه‌ی جدیدتری
   باشه، به کاربر یه دیالوگ با دکمه‌ی «دانلود نسخه جدید» نشون می‌ده. اگه
   `force_update: true` بذارید، کاربر تا آپدیت نکنه نمی‌تونه ادامه بده.

برای پخش خودِ فایل APK بین مدرسه‌ها هم کافیه لینک همون `apk_url` رو (یا از
تلگرام/گوگل‌درایو) به مدیرها بدید؛ گیرنده باید یه‌بار «نصب از منابع ناشناس»
رو تو تنظیمات گوشیش فعال کنه.
