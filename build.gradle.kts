import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
}

//DETEKT part
apply(plugin = libs.plugins.detekt.get().pluginId)

//analog of detekt{...} but without toolVersion
configure<DetektExtension> {
    //analyze content in src-folder of all modules
    source.setFrom(files(
        subprojects.map { it.projectDir }
                + file("src") // Include root project sources
    ))

    config.setFrom("${rootProject.projectDir}/detekt-setting/detekt.yml")
    baseline = file("${rootProject.projectDir}/detekt-setting/baseline.xml")

    // Disable default failure behavior (critical step!)
    ignoreFailures = false  // Keep false to ensure build fails
    autoCorrect = false     // Optional: Disable auto-fixing
    buildUponDefaultConfig = true // Respect default rules
}

//project:detekt will check kt/kts files from all project for generate only 1 report
tasks.withType<Detekt>{
    jvmTarget = JavaVersion.VERSION_21.toString()
    reports {
        html {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt-report.html"))
        }
        xml { required.set(false) }
        txt { required.set(false) }
        md { required.set(false) }
        sarif { required.set(false) }
    }

    // only kt and kts files check (without build/resources folders)
    include("**/*.kt", "**/*.kts")
    exclude("**/build/**", "**/resources/**")

    //task HOW... after detekt task
    finalizedBy("HOW_TO_FIX_DETEKT_FAIL_(IF_ERROR)")
}

//block with help-info and auto-open logic for report
tasks.register("HOW_TO_FIX_DETEKT_FAIL_(IF_ERROR)") {
    doLast {
        val reportPath = "${projectDir}/build/reports/detekt/detekt-report.html"
        val reportUrl = "file://${File(reportPath).absolutePath}"

        println("\nDetekt task ended")
        println("Report address (copy and paste to web-browser):")
        println(reportUrl)

        //extra help-info for detekt-fail in Exception-format
        if (tasks.named("detekt").get().state.failure != null) {
            autoOpenHtmlReport(reportPath)
            throw GradleException(detektInExceptionMessage(reportPath))
        } else {
            println("\nIn short, Detekt check accepted")
        }
    }
}

//replace app:detekt to project:detekt (which run after "silenced" app:detekt)
project(":app") {
    afterEvaluate {
        tasks.named("detekt") {
            enabled = false
            dependsOn(rootProject.tasks.named("detekt"))
        }
    }
}

subprojects {
    pluginManager.withPlugin("com.android.application") {
        //app:assembleDebug task exist after evaluate
        afterEvaluate {
            //app:detekt after app:assembleDebug
            tasks.named("assembleDebug") {
                finalizedBy("detekt")
            }
        }
    }

    //remove duplicates of Detekt tasks
    afterEvaluate {
        tasks.withType<Detekt>().configureEach {
            enabled = false
        }
    }
}

//util funs
fun Project.detektInExceptionMessage (reportPath: String): String = """
                
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
            
            """.trimIndent()

fun Project.autoOpenHtmlReport (reportPath: String) {
    val reportUrl = "file://${file(reportPath).absolutePath}"
    try {
        val os = System.getProperty("os.name").lowercase()

        when {
            os.contains("win") -> ProcessBuilder(
                "rundll32",
                "url.dll",
                "FileProtocolHandler",
                reportUrl
            ).start()

            os.contains("mac") -> ProcessBuilder("open", reportUrl).start()
            os.contains("nix") || os.contains("nux") -> ProcessBuilder(
                "xdg-open",
                reportUrl
            ).start()

            else -> println("Unsupported OS for automatic opening")
        }
    } catch (e: Exception) {
        println("Could not open browser automatically: ${e.message}")
    }
}