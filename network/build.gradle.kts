plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.bookbrowser.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "API_BASE_URL", "\"https://www.googleapis.com/books/v1/\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"https://www.googleapis.com/books/v1/\"")
        }
        release {
            consumerProguardFiles("consumer-rules.pro")
            buildConfigField("String", "API_BASE_URL", "\"https://www.googleapis.com/books/v1/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    debugImplementation(libs.okhttp.logging)
}
