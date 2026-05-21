plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.main"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.play.services.ads)
}
