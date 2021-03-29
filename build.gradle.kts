plugins {
    kotlin("js") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
}
val ktor_version="1.5.2"
val coroutines_version="1.4.2-native-mt"

group = "me.unity"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
    }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url= uri("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url=uri("https://dl.bintray.com/kotlin/ktor") }
    maven("https://dl.bintray.com/chainfire/maven")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    testImplementation(kotlin("test-js"))

    implementation("eu.chainfire:kotlin-js-threads:1.0.1")
    implementation(npm("setimmediate", "^1.0.5"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-js:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.30")
    implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.148-kotlin-1.4.30")
    implementation(npm("react", "17.0.1"))
    implementation(npm("react-dom", "17.0.1"))
    implementation("org.jetbrains:kotlin-styled:5.2.1-pre.148-kotlin-1.4.30")
    implementation(npm("styled-components", "~5.2.1"))

/*
    implementation("org.jetbrains:kotlin-react:16.13.1-pre.113-kotlin-1.4.0")
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.113-kotlin-1.4.0")
    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.113-kotlin-1.4.0")*/

    implementation("com.ccfraser.muirwik:muirwik-components:0.6.2")
   implementation(npm("@material-ui/core", "4.11.3"))
    implementation(npm("@material-ui/styles", "4.11.3"))


}

kotlin {
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
}