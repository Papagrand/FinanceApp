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
    }
}

rootProject.name = "FinanceApp"
include(":app")
include(":core")
include(":network")
include(":feature")
include(":feature:expenses")
include(":feature:account")
include(":feature:income")
include(":feature:categories")
include(":feature:settings")
include(":data")
include(":domain")
