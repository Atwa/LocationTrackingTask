@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

val composeVersion = extra.get("compose_version") as String
val hiltVersion = extra.get("hilt_version") as String
val coroutinesVersion = extra.get("coroutines_version") as String

android {
    namespace = "com.atwa.androidgpstask"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.atwa.androidgpstask"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }
    buildTypes.forEach {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        it.buildConfigField("String", "BASE_URL", "\"https://sos.srca.org.sa/api/test/\"")
        it.buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // compose ui
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")

    // kotlin
    implementation("androidx.core:core-ktx:1.12.0")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.25.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}