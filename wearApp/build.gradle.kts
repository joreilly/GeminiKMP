@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = 37
    defaultConfig {
        applicationId = "dev.johnoreilly.gemini"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        val localPropsFile = rootProject.file("local.properties")
        val localProperties = Properties()
        if (localPropsFile.exists()) {
            runCatching {
                localProperties.load(localPropsFile.inputStream())
            }.getOrElse {
                it.printStackTrace()
            }
        }

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"" + localProperties["GEMINI_API_KEY"] + "\"",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1",
                "/META-INF/versions/**"
            )
        }
    }

    namespace = "dev.johnoreilly.gemini"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        optIn.add("kotlin.RequiresOptIn")
        optIn.add("com.google.android.horologist.annotations.ExperimentalHorologistApi")
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.wear.compose.navigation)

    implementation(libs.compose.foundation)
    implementation(libs.horologist.composables)
    implementation(libs.horologist.compose.layout)
    implementation(libs.horologist.ai.ui)
    implementation(libs.markdown.renderer.core)
    implementation(libs.compose.material.iconsext)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.generativeai)
}
