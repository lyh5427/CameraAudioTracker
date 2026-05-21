plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.appdetail"
}

dependencies {
    implementation(project(":core:model"))
}
