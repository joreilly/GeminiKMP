import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {

    jvmToolchain(24)

    android {
        namespace = "dev.johnoreilly.gemini.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources { enable = true }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        all {
            languageSettings {
                optIn("nl.marc_apps.tts.experimental.ExperimentalVoiceApi")
                optIn("nl.marc_apps.tts.experimental.ExperimentalDesktopTarget")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.markdown.renderer)
            api(libs.compose.window.size)

            api(libs.generativeai)

            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.coil)
            // voyager is a multiplatform library for viewmodel navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            // storage data
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            // Time
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0-0.6.x-compat")
            implementation(libs.kotlinx.serialization.json)

        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation("nl.marc-apps:tts:2.5.0")
            }
        }

        iosMain.dependencies {}

        wasmJsMain.dependencies {
            implementation("nl.marc-apps:tts:2.5.0")
        }

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.johnoreilly.gemini"
            packageVersion = "1.0.0"
        }
    }
}

buildkonfig {
    packageName = "dev.johnoreilly.gemini"

    val localPropsFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropsFile.exists()) {
        runCatching {
            localProperties.load(localPropsFile.inputStream())
        }.getOrElse {
            it.printStackTrace()
        }
    }
    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "GEMINI_API_KEY",
            localProperties["gemini_api_key"]?.toString() ?: ""
        )
    }

}
