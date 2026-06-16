import java.util.Properties

plugins {
    alias(libs.plugins.king.android.application)
    alias(libs.plugins.king.android.application.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.yunho.king"

    val localProperties = Properties().apply {
        rootProject.file("local.properties").takeIf { it.exists() }?.reader(Charsets.UTF_8)?.use { load(it) }
    }

    fun Properties.getString(key: String): String =
        (getProperty(key) ?: "").trim().trim('"')

    val admobAppId = localProperties.getString("ADMOB_ID")
    val admobBannerId = localProperties.getString("ADMOB_UNIT_ID_MAIN_BANNER")
    val releaseKeyPath = localProperties.getString("RELEASE_KEY_PATH")
    val releaseKeyAlias = localProperties.getString("RELEASE_KEY_ALIAS")
    val releaseKeyPW = localProperties.getString("RELEASE_KEY_PW")
    val debugKeyPath = localProperties.getString("DEBUG_KEY_PATH")
    val debugKeyAlias = localProperties.getString("DEBUG_KEY_ALIAS")
    val debugKeyPW = localProperties.getString("DEBUG_KEY_PW")

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "ADMOB_UNIT_ID_MAIN_BANNER", "\"$admobBannerId\"")
        manifestPlaceholders["ADMOB_ID"] = admobAppId
        resValue("string", "ADMOB_UNIT_ID_MAIN_BANNER", admobBannerId)
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(debugKeyPath)
            storePassword = debugKeyPW
            keyAlias = debugKeyAlias
            keyPassword = debugKeyPW
        }
        create("release") {
            storeFile = file(releaseKeyPath)
            storePassword = releaseKeyPW
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPW
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":feature:launch"))
    implementation(project(":feature:navigator"))
    implementation(project(":feature:main"))
    implementation(project(":feature:intercept"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.service)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.play.services.ads)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler.androidx)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.video)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.guava.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test:core:1.6.1")
}
