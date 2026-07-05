plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.appdetail"
}

dependencies {
    implementation(project(":core:model"))
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.material:material-icons-extended")
}
