plugins {
    android("library")
    kotlin("android")
    bintray
    `bintray-release`
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        versionName = "$VERSION_ANDROIDX-alpha01"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDir("res")
            resources.srcDir("src")
        }
        getByName("androidTest") {
            setRoot("tests")
            manifest.srcFile("tests/AndroidManifest.xml")
            java.srcDir("tests/src")
            res.srcDir("tests/res")
            resources.srcDir("tests/src")
        }
    }
    lintOptions {
        isCheckTestSources = true
    }
    libraryVariants.all {
        generateBuildConfig?.enabled = false
    }
}

dependencies {
    implementation(material("$VERSION_ANDROIDX-alpha02"))

    testImplementation(junit())
    androidTestImplementation(kotlin("stdlib"))
    androidTestImplementation(androidx("core", "core-ktx", "$VERSION_ANDROIDX-alpha03"))
    androidTestImplementation(androidx("appcompat", version = "$VERSION_ANDROIDX-alpha01"))
    androidTestImplementation(androidx("test.espresso", "espresso-core", VERSION_ESPRESSO))
    androidTestImplementation(androidx("test", "runner", VERSION_RUNNER))
    androidTestImplementation(androidx("test", "rules", VERSION_RULES))
}

publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = RELEASE_ARTIFACT
    publishVersion = VERSION_ANDROIDX
    desc = RELEASE_DESC
    website = RELEASE_WEBSITE
}
