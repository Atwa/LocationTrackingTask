buildscript {
    allprojects {
        extra.apply {
            set("compose_version", "1.5.0")
            set("hilt_version", "2.49")
            set("coroutines_version", "1.6.4")
        }
    }

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.49")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}