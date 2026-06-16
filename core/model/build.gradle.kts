plugins {
    alias(libs.plugins.king.android.library)
}

android {
    namespace = "com.yunho.king.core.model"
}

dependencies {
    implementation(libs.androidx.room.runtime)
}
