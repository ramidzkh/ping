plugins {
    id("fabric-loom") version "0.4.33"
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.16.1")
    mappings("net.fabricmc", "yarn", "1.16.1+build.21", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.9.0+build.204")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.16.2+build.385-1.16.1")
}
