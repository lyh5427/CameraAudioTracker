pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("build-logic")
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
