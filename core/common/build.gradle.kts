plugins {
    alias(libs.plugins.king.android.library)
    alias(libs.plugins.king.android.library.compose)
    alias(libs.plugins.king.hilt)
}

android {
    namespace = "com.yunho.king.core.common"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.core)
}
