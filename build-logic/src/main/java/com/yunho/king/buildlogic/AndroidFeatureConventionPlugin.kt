package com.yunho.king.buildlogic

import com.yunho.king.buildlogic.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("king.android.library")
                apply("king.android.library.compose")
                apply("king.hilt")
            }

            dependencies {
                "implementation"(project(":domain"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:common"))
                "implementation"(libs.findLibrary("navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                "implementation"(libs.findLibrary("hilt-navigation-compose").get())
            }
        }
    }
}
