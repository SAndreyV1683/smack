plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "a.sboev.smack"
    compileSdk = 34

    defaultConfig {
        applicationId = "a.sboev.smack"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    //kapt("groupId:artifactId:version")
    //implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20-Beta2")
    //implementation ("org.igniterealtime.smack:smack-java8:4.4.0")
    // Optional for XMPPTCPConnection
    implementation ("org.igniterealtime.smack:smack-tcp:4.4.0")
    // Optional for XMPP-IM (RFC 6121) support (Roster, Threaded Chats, …)
    implementation ("org.igniterealtime.smack:smack-im:4.4.0")
    // Optional for XMPP extensions support
    //implementation ("org.igniterealtime.smack:smack-extensions:4.4.0")
    implementation ("org.igniterealtime.smack:smack-android-extensions:4.4.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



}

configurations {
    all{
        exclude("xpp3", "xpp3")
        exclude("xpp3", "xpp3_min")
    }
}

apply(plugin = "org.jetbrains.kotlin.kapt")


