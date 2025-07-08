import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.org.jline.utils.Log.debug
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.LinkedList
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

//DETEKT part
apply(plugin = libs.plugins.detekt.get().pluginId)
//apply(plugin = libs.detekt.formatting.get())

//analog of detekt{...} but without toolVersion
configure<DetektExtension> {
    //analyze content in src-folder of all modules
    source.setFrom(
        files(
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
tasks.withType<Detekt> {
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
    finalizedBy("HERE_DETEKT_REPORT_LINK")
}

//block with help-info and auto-open logic for report
fun inTaskMsg(hasFail: Boolean, source: String, reportPath: String): String {
    val resultMsg = if (hasFail) "there are failure findings" else "Detekt check accepted"
    return "${source[0].uppercase() + source.drop(1)} task ended\n" +
            "\nIn short, $resultMsg \n" +
            "\nConsolidated $source report available at (copy and paste to web-browser):\n" +
            "$reportPath"
}

tasks.register("HERE_DETEKT_REPORT_LINK") {
    doLast {
        val reportPath = "${projectDir}/build/reports/detekt/detekt-report.html"
        val hasFail = tasks.named("detekt").get().state.failure != null
        println("In, short " + if (hasFail) "there are failure detekt findings" else "Detekt check accepted")
        if (tasks.named("detekt").get().state.failure != null) {
            autoOpenHtmlReport(reportPath)
            throw GradleException(inTaskMsg(hasFail, "detekt", reportPath))
        }
    }
}

//replace app:detekt to project:detekt (which run after "silenced" app:detekt)
//add run project:consolidateKtlintReports after app:detekt
project(":app") {
    afterEvaluate {
        tasks.named("detekt") {
            enabled = false
            dependsOn(rootProject.tasks.named("detekt"))
            //while in comment: disable ktlint
            finalizedBy(rootProject.tasks.named("consolidateKtlintReports"))
        }
    }
}

//ktlint
var isKtlintAccepted = true
var ktlintConsolidateReportPath = "no_path"
apply(plugin = libs.plugins.jvm.get().pluginId)

tasks.register("consolidateKtlintReports") {
    group = "verification"
    description = "Consolidates all KTLint HTML reports into a single report"

    // Get all subprojects that have KTLint applied and Kotlin files
    val projectsWithKtlint = subprojects.filter { project ->
        project.plugins.hasPlugin("org.jlleitschuh.gradle.ktlint") &&
                project.fileTree("src").any { it.extension in listOf("kt", "kts") }
    }

    // Only depend on projects that actually have the ktlintCheck task
    dependsOn(projectsWithKtlint.map { it.tasks.named("ktlintCheck") })

    doLast {
        val outputDir = layout.buildDirectory.dir("reports/ktlint").get().asFile
        outputDir.mkdirs()

        val violationStore = HashMap<String, LinkedList<Violation>>()
        var reportCount = 0
        allprojects.forEach { project ->
            listOf("ktlintMainSourceSetCheck", "ktlintKotlinScriptCheck").forEach { checkReport ->
                if (project != rootProject && project.plugins.hasPlugin("org.jlleitschuh.gradle.ktlint")) {
                    reportCount++
                    val reportFile = project.layout.buildDirectory
                        .file("reports/ktlint/$checkReport/$checkReport.json")
                        .get().asFile
                    if (reportFile.exists()) {
                        violationsJsonMapper(reportFile.readText(), violationStore)
                    }
                }
            }
        }

        val reportText = getTotalKtlintHtml(reportCount, violationStore)
        val consolidatedReport = File(outputDir, "ktlint-report.html").apply {
            writeText(reportText)
        }

        isKtlintAccepted = violationStore.isEmpty()
        ktlintConsolidateReportPath = consolidatedReport.absolutePath
    }
    finalizedBy("HERE_KTLINT_REPORT_LINK")
}

tasks.register("HERE_KTLINT_REPORT_LINK") {
    doLast {
        println("In, short " + if (!isKtlintAccepted) "there are failure ktlint findings" else "ktlint check accepted")
        if (!isKtlintAccepted) {
            autoOpenHtmlReport(ktlintConsolidateReportPath)
            throw GradleException(
                inTaskMsg(
                    !isKtlintAccepted,
                    "ktlint",
                    ktlintConsolidateReportPath
                )
            )
        }
    }
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask> {
    reportsOutputDirectory.set(
        project.layout.buildDirectory.dir("reports/ktlint")
    )
}

fun Project.hasKotlinFiles(): Boolean {
    return fileTree("src").any { it.extension in listOf("kt", "kts") } ||
            fileTree(".").any { it.name == "build.gradle.kts" }
}

//test
//накидываем кастомную таску к тестам для авто-открытия окна с отчетом при наличии провала по тестам
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

subprojects {
    pluginManager.withPlugin("com.android.application") {
        //app:assembleDebug task exist after evaluate
        afterEvaluate {
            //app:detekt after app:assembleDebug
            tasks.named("assembleDebug") {
                finalizedBy("detekt")
                finalizedBy("test")
            }
        }
    }

    //remove duplicates of Detekt tasks
    afterEvaluate {
        tasks.withType<Detekt>().configureEach {
            enabled = false
        }
    }

    //ktlint
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        debug(true)
        ignoreFailures.set(true)
        reporters {
            reporter(ReporterType.JSON)
        }
    }
    if ((hasKotlinFiles())) {
        // Optionally configure plugin
        afterEvaluate {
            tasks.findByName("ktlintCheck")?.let {
                it.group = "verification"
            }
        }
    }
}

fun Project.autoOpenHtmlReport(reportPath: String) {
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

fun Project.getTotalKtlintHtml(
    reportCount: Int,
    violationStore: HashMap<String, LinkedList<Violation>>
): String {
    return buildString {
        append(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <style>
                    @import url('https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@400;500&display=swap');
                </style>
                <meta charset="utf-8">
                <title>ktlint report</title>
                <style>

                    body {
                        font-family: Arial, sans-serif;
                        font-weight: 400;
                    }

                    h1 {
                        font-family: Arial, sans-serif;
                        font-weight: 600;
                        width: fit-content;
                        background: -webkit-linear-gradient(left, rgba(105, 33, 224, 1), rgba(247, 44, 67, 1));
                        -webkit-background-clip: text;
                        -webkit-text-fill-color: transparent;
                    }

                    h2 {
                        font-family: Arial, sans-serif;
                        font-weight: 400;
                        background: rgb(105, 33, 224);
                        background: linear-gradient(90deg, rgba(105, 33, 224, 0.8) 0%, rgba(158, 41, 156, 0.8) 50%, rgba(247, 44, 67, 0.8) 100%);
                        padding: 0.2em;
                        color: #ffffff;
                        border-radius: 4px;
                    }

                    h3 {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(90deg, rgba(105, 33, 224, 0.4) 0%, rgba(158, 41, 156, 0.4) 50%, rgba(247, 44, 67, 0.4) 100%);
                        padding: 0.5em;
                        border-radius: 4px;
                    }

                    .rule-container {
                        background: linear-gradient(90deg, rgba(105, 33, 224, 0.2) 0%, rgba(158, 41, 156, 0.2) 100%);
                        border-radius: 4px;
                        padding-left: 1em;
                        padding-bottom: 0.3em;
                        padding-top: 0.3em;
                        padding-right: 1em;
                        line-height: 1.5em;
                        margin-bottom: 8px;
                        margin-top: 8px;
                    }

                    .rule {
                        font-family: Arial, sans-serif;
                        padding: 0.3em;
                        font-weight: 600;
                        border-radius: 3px;
                    }

                    .description {
                        font-family: Arial, sans-serif;
                        color: #000000;
                        padding: 0.3em;
                    }

                    .location {
                        font-family: Jetbrains Mono, monospace;
                        font-weight: 500;
                        color: #000000;
                        display: block;
                    }

                    .message {
                        font-family: Arial, sans-serif;
                        font-size: 0.8em;
                        color: #444444;
                        display: block;
                        margin-top: 1pt;
                    }

                    code {
                        font-family: JetBrains Mono, monospace;
                        font-weight: 400;
                    }

                    pre {
                        font-family: JetBrains Mono, monospace;
                        font-weight: 500;
                        border: 1px solid #e0e0e0;
                        overflow: auto;
                    }

                    .lineno {
                        margin-right: 24px;
                        color: #9771c3;
                        background-color: rgba(230, 211, 243, 0.51);
                    }

                    .error {
                        background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AwCFR4T/3uLMgAAADxJREFUCNdNyLERQEAABMCjL4lQwIzcjErpguAL+C9AvgKJDbeD/PRpLdm35Hm+MU+cB+tCKaJW4L4YBy+CAiLJrFs9mgAAAABJRU5ErkJggg==) bottom repeat-x;
                    }

                    .exception {
                        color: #b60808;
                        display: inline-block;
                        background-color: #ecdada;
                        border-color: #b60808;
                        border-radius: 10px;
                        border: solid 2px;
                        padding-left: 16px;
                        padding-right: 16px;
                        margin-bottom: 16px;
                    }
                </style>
            </head>
            <body>

            <?xml version="1.0" encoding="utf-8"?>
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="76.8263 47.4993 174.806 52.279" width="174.806px" height="52.279px">
              <defs>
                <linearGradient id="paint0_linear" x1="5.222e-07" y1="41.3361" x2="51.982" y2="33.29" gradientUnits="userSpaceOnUse" gradientTransform="matrix(1, 0, 0, 1, 344.153473, 219.52861)">
                  <stop stop-color="#7942EE"/>
                  <stop offset="1" stop-color="#FF4651"/>
                </linearGradient>
                <linearGradient id="paint1_linear" x1="53.0121" y1="51.5465" x2="60.9801" y2="30.2423" gradientUnits="userSpaceOnUse" gradientTransform="matrix(1, 0, 0, 1, 344.153473, 219.52861)">
                  <stop stop-color="#7942EE"/>
                  <stop offset="1" stop-color="#FF4651"/>
                </linearGradient>
              </defs>
              <a transform="matrix(1, 0, 0, 1, -267.32672119140625, -172.02969360351562)">
                <path d="M 435.431 262.218 L 425.446 246.96 L 435.1 237.054 L 426.487 237.054 L 418.064 246.209 L 418.064 227.946 L 410.871 227.946 L 410.871 262.218 L 418.064 262.218 L 418.064 254.566 L 420.572 251.937 L 427.197 262.218 L 435.431 262.218 Z" fill="#222222" style="stroke-width: 1;"/>
                <path d="M 453.32 260.762 L 453.32 255.035 C 452.279 255.598 451.096 255.927 449.771 255.927 C 448.162 255.927 447.452 255.129 447.452 253.485 L 447.452 242.735 L 453.415 242.735 L 453.415 236.631 L 447.452 236.631 L 447.452 230.2 L 440.259 230.2 L 440.259 236.631 L 437.231 236.631 L 437.231 242.735 L 440.259 242.735 L 440.259 254.659 C 440.259 260.481 443.241 262.218 447.641 262.218 C 450.055 262.218 451.806 261.654 453.32 260.762 Z" fill="#222222" style="stroke-width: 1;"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M 346.34 219.529 C 345.132 219.529 344.153 220.507 344.153 221.715 L 344.153 260.3 C 344.153 261.507 345.132 262.486 346.34 262.486 L 368.407 262.486 C 369.806 262.486 370.779 261.076 370.531 259.699 C 370.364 258.777 370.277 257.827 370.277 256.857 C 370.277 248.073 377.398 240.953 386.181 240.953 C 387.022 240.953 387.848 241.018 388.654 241.144 C 390.011 241.355 391.373 240.387 391.373 239.013 L 391.373 221.715 C 391.373 220.507 390.395 219.529 389.187 219.529 L 346.34 219.529 Z" fill="url(#paint0_linear)" style="stroke-width: 1;"/>
                <path d="M 399.489 268.145 L 398.921 267.577 L 397.396 266.052 L 397.105 265.761 C 396.396 265.052 396.339 263.935 396.826 263.059 C 397.783 261.34 398.33 259.362 398.33 257.258 C 398.33 252.439 395.468 248.279 391.356 246.379 C 390.673 246.063 389.956 245.809 389.211 245.626 C 388.517 245.455 387.8 245.345 387.065 245.301 C 386.828 245.287 386.59 245.278 386.35 245.278 C 382.67 245.278 379.374 246.947 377.175 249.567 C 376.132 250.809 375.337 252.264 374.865 253.858 C 374.545 254.937 374.37 256.077 374.37 257.258 C 374.37 257.558 374.385 257.855 374.407 258.149 C 374.462 258.885 374.58 259.602 374.762 260.295 C 374.957 261.04 375.222 261.758 375.551 262.44 C 377.487 266.458 381.6 269.238 386.35 269.238 C 388.11 269.238 389.783 268.855 391.289 268.169 C 392.138 267.783 393.158 267.882 393.817 268.541 L 394.243 268.967 L 395.784 270.508 L 396.455 271.18 C 396.874 271.599 397.423 271.808 397.972 271.808 C 398.521 271.808 399.07 271.599 399.489 271.18 C 400.327 270.342 400.327 268.983 399.489 268.145 Z M 386.35 264.947 C 384.106 264.947 382.083 263.98 380.677 262.441 C 380.102 261.812 379.631 261.088 379.288 260.295 C 378.998 259.624 378.803 258.903 378.715 258.15 C 378.681 257.857 378.662 257.56 378.662 257.258 C 378.662 253.019 382.111 249.57 386.35 249.57 C 386.591 249.57 386.83 249.583 387.065 249.604 C 387.817 249.674 388.537 249.853 389.211 250.124 C 389.999 250.441 390.722 250.885 391.357 251.431 C 392.997 252.842 394.039 254.93 394.039 257.258 C 394.038 261.497 390.59 264.947 386.35 264.947 Z" fill="url(#paint1_linear)" style="stroke-width: 1;"/>
                <path d="M 354.147 245.227 C 353.496 245.227 353 245.024 352.659 244.62 C 352.319 244.2 352.148 243.698 352.148 243.116 L 352.148 243.019 C 352.148 242.437 352.319 241.943 352.659 241.539 C 353 241.119 353.496 240.908 354.147 240.908 C 354.798 240.908 355.294 241.119 355.635 241.539 C 355.976 241.943 356.146 242.437 356.146 243.019 L 356.146 243.116 C 356.146 243.698 355.976 244.2 355.635 244.62 C 355.294 245.024 354.798 245.227 354.147 245.227 Z M 352.326 230.344 L 352.326 227.523 L 355.968 227.523 L 355.968 230.344 L 354.813 238.69 L 353.481 238.69 L 352.326 230.344 Z" fill="white" style="stroke-width: 1;"/>
                <path d="M 364.426 245.227 C 363.775 245.227 363.279 245.024 362.938 244.62 C 362.598 244.2 362.428 243.698 362.428 243.116 L 362.428 243.019 C 362.428 242.437 362.598 241.943 362.938 241.539 C 363.279 241.119 363.775 240.908 364.426 240.908 C 365.078 240.908 365.574 241.119 365.914 241.539 C 366.255 241.943 366.425 242.437 366.425 243.019 L 366.425 243.116 C 366.425 243.698 366.255 244.2 365.914 244.62 C 365.574 245.024 365.078 245.227 364.426 245.227 Z M 362.605 230.344 L 362.605 227.523 L 366.247 227.523 L 366.247 230.344 L 365.093 238.69 L 363.76 238.69 L 362.605 230.344 Z" fill="white" style="stroke-width: 1;"/>
                <path d="M 463.408 262.218 L 463.408 227.946 L 456.215 227.946 L 456.215 262.218 L 463.408 262.218 Z" fill="#222222" style="stroke-width: 1;"/>
                <path d="M 474.401 262.218 L 474.401 240.208 L 467.208 240.208 L 467.208 262.218 L 474.401 262.218 Z" fill="#222222" style="stroke-width: 1;"/>
                <path d="M 474.401 234.759 L 474.401 227.946 L 467.208 227.946 L 467.208 234.759 L 474.401 234.759 Z" fill="#222222" style="stroke-width: 1;"/>
                <path d="M 518.864 260.762 L 518.864 255.035 C 517.823 255.598 516.64 255.927 515.315 255.927 C 513.706 255.927 512.996 255.129 512.996 253.485 L 512.996 242.735 L 518.959 242.735 L 518.959 236.631 L 512.996 236.631 L 512.996 230.2 L 505.803 230.2 L 505.803 236.631 L 502.775 236.631 L 502.775 242.735 L 505.803 242.735 L 505.803 254.659 C 505.803 260.481 508.785 262.218 513.185 262.218 C 515.599 262.218 517.35 261.654 518.864 260.762 Z" fill="#222222" style="stroke-width: 1;"/>
                <path d="M 477.201 262.439 L 477.201 237.04 L 484.394 237.04 L 484.394 238.687 C 485.729 237.903 487.35 237.04 489.196 237.04 C 490.962 237.04 493.315 237.577 495.146 238.397 C 496.1 238.739 497.425 239.47 498.697 241.031 C 500.833 243.652 500.974 249.235 500.974 249.235 L 500.974 262.516 L 493.781 262.516 L 493.781 245.892 C 491.223 242.324 488.387 243.845 486.8 243.845 C 486.192 243.845 485.176 244.651 484.394 245.339 L 484.394 262.439 L 477.201 262.439 Z" fill="#222222" style="stroke-width: 1;"/>
              </a>
            </svg>

            <h2>Metrics</h2>

            <div>
              <ul>
                <li>$reportCount checked report(s)</li>
              </ul>
            </div>


            <h2>Findings</h2>
            <div>Unresolved rules: ${violationStore.keys.size}</div>
            <div>Total: ${violationStore.keys.sumOf { k -> violationStore[k]?.size ?: 0 }}</div>
            """.trimIndent()
        )

        for ((rule, violations) in violationStore) {
            append(
                """
                <details id="$rule" open="open">
                <summary class="rule-container"><span class="rule">$rule: ${violations.size} </span></summary>
                <ul>      
                """.trimIndent()
            )
            for (v in violations) {
                appendLine(
                    "<li>" +
                            "<span class=\"location\">${v.file}:${v.line}:${v.column}" +
                            "<span class=\"message\">${v.message}</span>" +
                            "</li>"
                )
                val absoluteFile = File(v.file)
                if (absoluteFile.exists()) {
                    absoluteFile.useLines {
                        append(snippetCode(it, v.line, v.column))
                    }
                }
            }
            appendLine("</ul></details>")
        }
        appendLine(
            "generated with ktlint version ${libs.versions.ktlintGradle.get()} on ${
                java.time.LocalDateTime.now().toString().take(19)
            }"
        )
        appendLine("</body></html>")
    }
}

//like json-schema of module-report in ktlint
data class FileReport(
    val file: String,
    @SerializedName("errors") val violations: List<Violation>
)

data class Violation(
    val file: String = "",
    val line: Int,
    val column: Int,
    val message: String,
    val rule: String
)

fun violationsJsonMapper(
    json: String,
    violationStore: HashMap<String, LinkedList<Violation>>
) {
    val fileReportArray = toFileReports(json)
    for (fileReport in fileReportArray) {
        for (v in fileReport.violations) {
            val rule = v.rule.substring(9)//without "standart:"
            violationStore.putIfAbsent(rule, LinkedList())
            violationStore[rule]!!.add(v.copy(file = fileReport.file, rule = rule))
        }
    }
}

fun toFileReports(json: String): Array<FileReport> =
    Gson().fromJson(json, Array<FileReport>::class.java)

//without error show
fun snippetCode(
    lines: Sequence<String>,
    errLine: Int,
    errCol: Int,
    extraLines: Int = 2
): String {
    val dropLineCount = max(errLine - 1 - extraLines, 0)
    val takeLineCount = extraLines + 1 + min(errLine - 1, extraLines)
    var currentLineNumber = dropLineCount + 1
    var errorLength = 1

    val builder = StringBuilder()
    builder.append("<pre><code>")

    lines
        .drop(dropLineCount)
        .take(takeLineCount)
        .forEach { line ->
            builder.append(
                "<span class=\"lineno\">${
                    "%1$4s".format(
                        Locale.ROOT,
                        currentLineNumber
                    )
                } </span>"
            )
            if (currentLineNumber >= errLine && errorLength > 0) {
                val column = if (currentLineNumber == errLine) errCol - 1 else 0
                errorLength -= builder.writeErrorLine(
                    line,
                    column,
                    errorLength
                ) + 1 // we need to consume the \n
            } else {
                builder.append(escapeHtml(line))
            }
            builder.append("\n")
            currentLineNumber++
        }

    builder.append("</code></pre>")

    return builder.toString()
}

fun StringBuilder.writeErrorLine(
    line: String,
    errorStarts: Int,
    length: Int
): Int {
    val errorEnds = min(errorStarts + length, line.length)
    if (line.isNotBlank()) {
        append(line.substring(startIndex = 0, endIndex = errorStarts))
        append(
            "<span class=\"error\">${
                line.substring(
                    startIndex = errorStarts,
                    endIndex = errorEnds
                )
            }" +
                    "</span>"
        )
        append(line.substring(startIndex = errorEnds))
    } else {
        append("<span class=\"error\"> </span>")
    }
    return errorEnds - errorStarts
}

private fun escapeHtml(text: String): String =
    text.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
