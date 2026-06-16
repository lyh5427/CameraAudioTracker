plugins {
    alias(libs.plugins.king.android.feature)
}

android {
    namespace = "com.yunho.king.feature.navigator"
}

dependencies {
    implementation(project(":feature:launch"))
    implementation(project(":feature:main"))
    implementation(project(":feature:appdetail"))
    implementation(project(":feature:intercept"))
}
