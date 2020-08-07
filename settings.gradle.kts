rootProject.name = "ping"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}
