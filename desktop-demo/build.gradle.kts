import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // 版本号由 settings.gradle.kts 加载 gradle.properties 后给出
    kotlin("jvm") // version "1.9.0"
    id("org.jetbrains.compose") // version "1.5.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    api(compose.foundation)
    api(compose.animation)

    val precomposeVersion="1.5.4"
    api("moe.tlaster:precompose:$precomposeVersion")
    api("moe.tlaster:precompose-molecule:$precomposeVersion") // For Molecule intergration
    api("moe.tlaster:precompose-viewmodel:$precomposeVersion") // For ViewModel intergration
    api("moe.tlaster:precompose-koin:$precomposeVersion") // For Koin intergration
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "desktop-demo"
            packageVersion = "1.0.0"
        }
    }
}
