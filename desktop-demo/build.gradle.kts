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

    val ktorVersion = "2.3.5"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            // 每种格式都依赖特定的打包工具。
            // gradle 的 compose desktop 分组下的 package* 任务
            targetFormats(
                // mac
                TargetFormat.Dmg, // 不支持交叉编译，在 windows 提示 （packageDmg SKIPPED）跳过了。
                //TargetFormat.Pkg, // 这个 windows 下会报错
                // windows
                TargetFormat.Msi, // 使用的 Wix 打包，会自动下载打包工具，要确保网络通畅。 安装后没 uninstall.exe 但是控制面板可以删除。。。。
                TargetFormat.Exe, // 使用的 Wix 打包，会自动下载打包工具，要确保网络通畅。 安装后没 uninstall.exe 但是控制面板可以删除。。。。
                // linux
                TargetFormat.Deb, // 不支持交叉编译，在 windows 提示 （packageDeb SKIPPED）跳过了。
                TargetFormat.Rpm, // 不支持交叉编译，在 windows 提示 （packageRpm SKIPPED）跳过了。
            )
            packageName = "desktop-demo"
            packageVersion = "1.0.0"
        }
    }
}
