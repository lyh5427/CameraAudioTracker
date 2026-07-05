pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "King"
include(":app")
include(":core:model")
include(":core:common")
include(":core:designsystem")
include(":data")
include(":domain")
include(":feature:launch")
include(":feature:navigator")
include(":feature:main")
include(":feature:appdetail")
include(":feature:intercept")
