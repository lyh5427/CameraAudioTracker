package com.yunho.king.buildlogic.extensions

import com.yunho.king.buildlogic.const.BuildConst
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = BuildConst.COMPILE_SDK

        defaultConfig {
            minSdk = BuildConst.MIN_SDK
        }

        compileOptions {
            sourceCompatibility = BuildConst.JAVA_VERSION
            targetCompatibility = BuildConst.JAVA_VERSION
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(BuildConst.JDK_VERSION)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
