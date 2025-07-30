pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "YeaHub"
include(":app")
include(":core:network-api")
include(":core:network-impl")
include(":core:ui")
include(":core:test")
include(":core:common")
include(":core:utils")
include(":core:navigation-api")
include(":core:navigation-impl")
include(":feature")
include(":feature:example-profile")
include(":feature:example-profile:api")
include(":feature:example-profile:impl")
include(":feature:example-home")
include(":feature:example-home:api")
include(":feature:example-home:impl")
include(":feature:example-questions")
include(":feature:example-questions:api")
include(":feature:example-questions:impl")
include(":feature:detail-question")
include(":feature:detail-question:api")
include(":feature:detail-question:impl")
include(":feature:example-details")
include(":feature:example-details:api")
include(":feature:example-details:impl")
