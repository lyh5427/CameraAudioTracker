plugins {
    alias(libs.plugins.king.android.library)
    alias(libs.plugins.king.android.library.compose)
}

android {
    namespace = "com.yunho.king.core.designsystem"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
}
