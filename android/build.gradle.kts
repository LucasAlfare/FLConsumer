@file:Suppress("LocalVariableName")

plugins {
  kotlin("android")
  id("com.android.application")
  id("org.jetbrains.compose")
}

dependencies {
  val lucasalfare_fllistening_version: String by project
  val ktor_version: String by project

  implementation("androidx.activity:activity-compose:1.7.2")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.core:core-ktx:1.10.1")
  implementation("androidx.compose.animation:animation:1.4.3")
  implementation("androidx.compose.material:material:1.4.3")

  implementation("io.coil-kt:coil-compose:2.4.0")

  implementation("com.google.code.gson:gson:2.10.1")

  implementation("io.ktor:ktor-client-core:$ktor_version")
  implementation("io.ktor:ktor-client-cio:$ktor_version")

  implementation("com.github.LucasAlfare:FLListening:$lucasalfare_fllistening_version")
}

android {
  namespace = "com.lucasalfare.flconsumer"
  compileSdk = 33
  defaultConfig {
    applicationId = "com.lucasalfare.flconsumer"
    minSdk = 26
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}