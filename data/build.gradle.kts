plugins {
    alias(libs.plugins.king.android.library)
    alias(libs.plugins.king.hilt)
}

android {
    namespace = "com.yunho.king.data"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.coroutines.core)
}
