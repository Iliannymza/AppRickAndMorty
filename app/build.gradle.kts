plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.apprickandmorty"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.apprickandmorty"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }
    // se agrega para llamar al binding
    buildFeatures {
        viewBinding = true
    }

    // esta tambien se agrega
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.play.services.awareness)
    // Retrofit
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Dependencia de Glide para la carga de imágenes
    implementation ("com.github.bumptech.glide:glide:4.16.0") // Usa la última versión estable
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0") // Necesario para algunas funcionalidades avanzadas y para evitar errores

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}