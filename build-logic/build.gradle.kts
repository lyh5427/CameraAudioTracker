plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.hilt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "king.android.application"
            implementationClass = "com.yunho.king.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "king.android.application.compose"
            implementationClass = "com.yunho.king.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "king.android.library"
            implementationClass = "com.yunho.king.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "king.android.library.compose"
            implementationClass = "com.yunho.king.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureCompose") {
            id = "king.android.feature"
            implementationClass = "com.yunho.king.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = "king.hilt"
            implementationClass = "com.yunho.king.buildlogic.HiltConventionPlugin"
        }
    }
}
