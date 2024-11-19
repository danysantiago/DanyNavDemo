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
        maven(url = uri("https://androidx.dev/snapshots/builds/12678803/artifacts/repository"))
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://androidx.dev/snapshots/builds/12678803/artifacts/repository"))
    }
}

rootProject.name = "DanyNavDemo"
include(":app")
include(":appScope")
include(":colorScreen")
