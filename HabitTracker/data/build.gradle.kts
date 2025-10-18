plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.ostrovskiy.habittracker.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":Domain"))
    // Room
    val room_version = "2.6.1" // Можно использовать актуальную версию
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("com.google.code.gson:gson:2.10.1")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0")) // Убедитесь, что версия совпадает с той, что в модуле app
    implementation("com.google.firebase:firebase-auth")
    // Библиотеки для работы с сетью
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}