tasks.register<Delete>("clean") {
    group = "build"
    delete(layout.buildDirectory)
}

tasks.register<Sync>("dist") {
    group = "build"
    dependsOn(":integrations:app:assembleRelease")
    dependsOn(":patches:dist")
    from(project(":integrations:app").layout.buildDirectory.dir("outputs/apk/release")) {
        include("*-$version.apk")
    }
    from(project(":patches").layout.buildDirectory.dir("libs")) {
        include("*-$version.jar")
    }
    from(project(":patches").layout.projectDirectory) {
        include("patches.json")
    }
    into(layout.buildDirectory)
}
