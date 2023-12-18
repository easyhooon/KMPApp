@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.skieTouchlab)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    // As of right now, there is no a direct way to compile our Kotlin code to Swift
    // Instead, we need to do it over Objective-C because of some compatibility issues related to iOS.
    // when build project, that Objective-C generated code will be visible in the build directory
    // that file contains the chode which is compiled by the Kotlin Compiler to Objective-C
    // TouchLab 의 SKIE 라이브러리의 도움을 받음 -> Kotlin 의 API줌(coroutine, flow, suspend func, sealed class, default argument...etc)
    // 들을 Swift 기반의 iOS 에서 사용하는 형태로 변환해줌 (flow wrapper 를 사용할 필요 x)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // SKIE https://skie.touchlab.co/intro
            implementation(libs.skie.annotations)
            // Kotlin multiplatform library for logging
            implementation(libs.kermit)
        }
        androidMain.dependencies {
            // for Native Http Client Engines
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            // for Native Http Client Engines
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.kenshi.mobiledemo"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
