pluginManagement {
    val flutterSdkPath = run {
        val properties = java.util.Properties()
        file("local.properties").inputStream().use { properties.load(it) }
        val flutterSdkPath = properties.getProperty("flutter.sdk")
        require(flutterSdkPath != null) { "flutter.sdk not set in local.properties" }
        flutterSdkPath
    }

    includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")

    repositories {
        // ─── Mirror های ایرانی ──────────────────────────────
        maven { url = uri("https://maven.myket.ir") }
        maven { url = uri("https://maven.devneeds.ir") }
        maven { url = uri("https://gradle.iranrepo.ir") }
        maven { url = uri("https://gradle.jamko.ir") }
        maven { url = uri("https://en-mirror.ir") }
        maven { url = uri("https://archive.ito.gov.ir/gradle/maven-plugin/") }
        // ─── Mirror های چینی (Aliyun) ─────────────────────
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // ─── منابع رسمی (Fallback) ────────────────────────
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("dev.flutter.flutter-plugin-loader") version "1.0.0"
    id("com.android.application") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
}

dependencyResolutionManagement {
    // ⭐ تغییر کلیدی: از PREFER_SETTINGS استفاده کن تا Flutter بتواند مخزن خودش را اضافه کند
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        // ─── Mirror های ایرانی ──────────────────────────────
        maven { url = uri("https://maven.myket.ir") }
        maven { url = uri("https://maven.devneeds.ir") }
        maven { url = uri("https://gradle.iranrepo.ir") }
        maven { url = uri("https://gradle.jamko.ir") }
        maven { url = uri("https://en-mirror.ir") }
        maven { url = uri("https://archive.ito.gov.ir/gradle/maven-plugin/") }
        // ─── Mirror های چینی (Aliyun) ─────────────────────
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // ─── مخزن اختصاصی Flutter (بسیار مهم) ───────────────
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
        // ─── منابع رسمی (Fallback) ────────────────────────
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "nexa"
include(":app")