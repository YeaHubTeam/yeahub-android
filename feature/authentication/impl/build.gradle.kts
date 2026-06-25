plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.yeahub.authentication.impl"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.jvmArgs("-XX:+EnableDynamicAgentLoading")
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(project(":core:datastore-api"))
    implementation(project(":core:navigation-api"))
    implementation(project(":core:network-api"))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.android)

    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Navigation dependencies
    implementation(libs.compose.shimmer)
    implementation(libs.androidx.navigation.compose)

    // Timber
    implementation(libs.timber)
    implementation(libs.androidx.ui.tooling.preview.android)

    // Retrofit (for HttpException)
    implementation(libs.retrofit.core)
    implementation(libs.gson)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.junit.jupiter)
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly(libs.junit.platform.launcher)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
