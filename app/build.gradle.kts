import com.android.build.api.variant.BuildConfigField

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.anibalcopitan.reservatotal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.anibalcopitan.reservatotal"
        minSdk = 23
        targetSdk = 33
        versionCode = 5
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

//          --- config
//
//            buildConfigField("String", "BUILD_TIME", "\"0\"")
//            resValue("string", "build_time", "0")
//
//          --- access in kotlin code ----
//
//          val apiURL = BuildConfig.hola
//          val apiUrl = getString(R.string.api_base_url)

            /**
             * Remove these variables is not used (USEFULL for the future)
             * current use api = MainActivity::API_OKEYPAY
             */
            buildConfigField("String", "VITE_API_BASE_URL_APP", "\"https://script.google.com/macros/s/AKfycbxOfihyZFGENIxt8MRKpwaDvpmcOilIsB8bZgOMLFK5qHXOew_XMao2DemUk3pB-N2KEw/exec\"")
            buildConfigField("String", "VITE_BASE_URL_WEB", "\"https://reservatotal.anibalcopitan.com/\"")
            signingConfig = signingConfigs.getByName("debug")

        }

        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true

            /**
             * Remove these variables is not used (USEFULL for the future)
             * current use api = MainActivity::API_OKEYPAY
             */
            buildConfigField("String", "VITE_API_BASE_URL_APP", "\"https://script.google.com/macros/s/AKfycbxOfihyZFGENIxt8MRKpwaDvpmcOilIsB8bZgOMLFK5qHXOew_XMao2DemUk3pB-N2KEw/exec\"")
            buildConfigField("String", "VITE_BASE_URL_WEB", "\"http://192.168.1.27:4000/\"")
        }

        /**
         * The `initWith` property lets you copy configurations from other build types,
         * then configure only the settings you want to change. This one copies the debug build
         * type, and then changes the manifest placeholder and application ID.
         */
        /*
        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debugStaging"
        }
        */
    }

    /**
     *
     * Creation multiple version like:
     * - Free (demo)
     * - Premium (full)
     */
    /*
    flavorDimensions += listOf("version")
    productFlavors {
        create("demo") {
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }
        create("full") {
            dimension = "version"
            applicationIdSuffix = ".full"
            versionNameSuffix = "-full"
        }
    }
    */

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    val nav_version = "2.7.0"
    val composeVersion = "2024.02.02" // Cambia esto por la última versión
    val composeVersionUI = "1.5.4"

    implementation("androidx.core:core-ktx:1.9.0")
  implementation(platform("androidx.compose:compose-bom:$composeVersion"))

    implementation("androidx.compose.ui:ui:$composeVersionUI")
    implementation("androidx.compose.ui:ui-graphics:$composeVersionUI")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersionUI")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Java language implementation
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")


    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:$composeVersion"))
    // errror dependencias reportado por google play
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersionUI")

    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersionUI")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersionUI")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation(kotlin("script-runtime"))

    /*
    * PLAYSTORE required updated (for update the app)
    * */
    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:feature-delivery:2.1.0")

    // For Kotlin users, also import the Kotlin extensions library for Play Feature Delivery:
    implementation("com.google.android.play:feature-delivery-ktx:2.1.0")

    /**
     * Integrate Play Integrity API into your app
     * required by google play
     */
    implementation ("com.google.android.play:integrity:1.3.0")
}

/***
 * debuging error: java.lang.NoSuchFieldError: No field Key of type
 */
val coroutinesVersion = "1.8.0" // check version in app gradle file!

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    }
}
