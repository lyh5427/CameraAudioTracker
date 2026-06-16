package com.yunho.king.buildlogic

import com.yunho.king.buildlogic.const.BuildConst
import com.yunho.king.buildlogic.extensions.configureKotlin
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(plugins) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("king.hilt")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlin(this)

                defaultConfig.apply {
                    applicationId = BuildConst.APPLICATION_ID
                    minSdk = BuildConst.MIN_SDK
                    targetSdk = BuildConst.TARGET_SDK
                    versionCode = BuildConst.VERSION_CODE
                    versionName = BuildConst.VERSION_NAME
                }
            }
        }
    }
}
