plugins {
    alias(libs.plugins.android.application)
}


android {
    namespace = "com.umt.ecommerce"
    compileSdk = 35

    buildFeatures {
        viewBinding=true
    }

    defaultConfig {
        applicationId = "com.umt.ecommerce"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    /* For loading images from internet*/
    implementation("com.github.bumptech.glide:glide:4.16.0")
    /* For rounded image view*/
    implementation ("com.makeramen:roundedimageview:2.3.0")
    /* For search bar*/
    implementation ("com.github.mancj:MaterialSearchBar:0.8.5")
    /* For slider start*/
    // Material Components for Android. Replace the version with the latest version of Material Components library.
    implementation ("com.google.android.material:material:1.5.0")

    // Circle Indicator (To fix the xml preview "Missing classes" error)
    implementation ("me.relex:circleindicator:2.1.6")

    implementation ("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.1")
    /* For slider end*/
    implementation ("com.android.volley:volley:1.2.1")

    implementation ("com.github.hishd:TinyCart:1.0.1")

    implementation ("com.github.delight-im:Android-AdvancedWebView:v3.2.1")

    implementation ("androidx.core:core:1.12.0")



}