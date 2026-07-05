plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.main"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.play.services.ads)
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.material:material-icons-extended")
}
