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
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "FinanceApp"
include(":app")
include(":core")
include(":feature")
include(":feature:account")
include(":feature:categories")
include(":feature:settings")
include(":core:navigation")
include(":feature:transactions")
include(":core:data")
include(":core:data:api")
include(":core:data:impl")
include(":core:ui")
include(":core:utils")
include(":core:data:local")
