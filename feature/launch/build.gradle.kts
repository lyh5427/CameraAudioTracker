plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.launch"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}
