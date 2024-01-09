import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

plugins {
    alias(libs.plugins.androidApplication)
    kotlin("android")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "dev.johnoreilly.gemini"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"" + localProperties["GEMINI_API_KEY"] + "\"",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        this.jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.annotations.ExperimentalHorologistApi"
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
    sourceSets.all {
        languageSettings {
            optIn("kotlin.RequiresOptIn")
        }
    }
}

dependencies {
    implementation(libs.compose.foundation)
    implementation("com.google.android.horologist:horologist-composables:0.5.18")
    implementation("com.google.android.horologist:horologist-compose-layout:0.5.18")
    implementation("com.google.android.horologist:horologist-ai-ui:0.5.18")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation(libs.kotlinx.coroutines)
    implementation(libs.bundles.ktor.common)
    implementation(libs.ktor.client.okhttp)
}
