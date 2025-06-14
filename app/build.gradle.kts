import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
}

android {
    namespace = "ru.yeahub"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.yeahub"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// default lint config - only warnings
android {
    lint {
        checkDependencies = true  // Enable Lint for dependencies
        htmlOutput = file("${layout.buildDirectory.get()}/reports/lint/lint-report.html")
    }
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files()) // Empty = use defaults detekt rules
    baseline = file("$projectDir/detekt-setting/baseline.xml")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_21.toString()

    // Disable default failure behavior (critical step!)
    ignoreFailures = false  // Keep false to ensure build fails
    autoCorrect = false     // Optional: Disable auto-fixing

    reports {
        html {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.html"))
        }

        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }

    finalizedBy(detektMessage)
}

val detektMessage by tasks.register("HOW TO FIX DETEKT FAIL") {
    doLast {
        val reportPath = "${layout.buildDirectory.get()}/reports/detekt/detekt-report.html"
        val reportUrl = "file://${File(reportPath).absolutePath}"

        println("\nDetekt task ended")
        println("Report address (copy and paste to web-browser):")
        println(reportUrl)

        if (tasks.named("detekt").get().state.failure != null) {
            autoOpenHtmlReport(reportPath)
            throw GradleException("""
                
            ________________________________________________________________________________________   
            📃FULL REPORT (🌐 copy and paste to web-browser):
            $reportPath
            ________________________________________________________________________________________
            🔧 HOW TO FIX? 
            1. Open the report and fix the listed issues.
            2. Put changes.
            3. Re-build project (preferably) OR run locally (terminal/Git Bash)
                    ./gradlew detekt
            
            Optionally (pre-debug mode): Try locally (terminal/Git Bash)
                    ./gradlew build -x detekt -x lint
            to see app running without detekt and lint

            ⚠️ The default build will fail until all issues are resolved.
            ________________________________________________________________________________________
            
            """.trimIndent())
        }
    }
}

afterEvaluate {
    tasks.named("assembleDebug") {
        finalizedBy("test")
        finalizedBy("detekt")
        finalizedBy("lint")
    }
}

tasks.withType<Test> {
    finalizedBy("openTestReportOnFailure")
}

tasks.register("openTestReportOnFailure") {
    onlyIf {
        tasks.withType<Test>().any { it.state.failure != null }
    }
    doLast {
        val reportPath = "${projectDir}/build/reports/tests/testDebugUnitTest/index.html"
        autoOpenHtmlReport(reportPath)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit.jupiter)
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.mockk)

    val detekt_version = "1.23.8"
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detekt_version")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:$detekt_version")

}

fun autoOpenHtmlReport (reportPath: String) {
    val reportUrl = "file://${file(reportPath).absolutePath}"
    try {
        val os = System.getProperty("os.name").lowercase()

        when {
            os.contains("win") -> ProcessBuilder("rundll32", "url.dll", "FileProtocolHandler", reportUrl).start()
            os.contains("mac") -> ProcessBuilder("open", reportUrl).start()
            os.contains("nix") || os.contains("nux") -> ProcessBuilder("xdg-open", reportUrl).start()
            else -> println("Unsupported OS for automatic opening")
        }
    } catch (e: Exception) {
        println("Could not open browser automatically: ${e.message}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}