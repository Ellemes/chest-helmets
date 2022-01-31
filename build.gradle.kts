plugins {
    id("fabric-loom").version("0.11.27").apply(false)
}

val javaVersion = JavaVersion.VERSION_17
val jetbrainsAnnotationsVersion = "23.0.0"
val minecraftVersion = "1.18.1"
val yarnVersion = "22"

val requiredGradleVersion = "7.4-rc-1"
if (!(gradle.startParameter.taskNames.any { "wrapper" in it }) && gradle.gradleVersion != requiredGradleVersion) {
    throw IllegalStateException("Gradle version must be $requiredGradleVersion, found ${gradle.gradleVersion}; to update use: ./gradlew :wrapper --gradle-version=$requiredGradleVersion")
}

tasks.register("buildMod") {

}

subprojects {
    apply(plugin = "java-library")

    val platform : String = project.property("template.platform") as String
    val projectUsesDatagen : Boolean = project.hasProperty("template.uses_datagen")
    val projectProducesReleaseArtifact : Boolean = project.hasProperty("template.produces_artifact")

    rootProject.tasks.getByName("buildMod") {
        if (projectProducesReleaseArtifact) {
            dependsOn(this@subprojects.tasks.getByName("build"))
        }
    }

    val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class)

    group = "ninjaphenix"
    version = "${properties["mod_version"]}+$minecraftVersion"
    project.extensions.getByType(BasePluginExtension::class).apply {
        archivesName.set("${properties["archives_base_name"]}")
    }
    buildDir = rootDir.resolve("build/${project.name}")

    project.extensions.getByType(JavaPluginExtension::class).apply {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    dependencies {
        add("implementation", "org.jetbrains:annotations:$jetbrainsAnnotationsVersion")
    }

    if (platform == "fabric") {
        apply(plugin = "fabric-loom")

        if (projectUsesDatagen) {
            javaPluginExtension.sourceSets {
                named("main") {
                    resources {
                        srcDir("src/main/generated")
                    }
                }

                create("datagen") {
                    compileClasspath += named("main").get().compileClasspath
                    runtimeClasspath += named("main").get().runtimeClasspath
                    compileClasspath += named("main").get().output
                    runtimeClasspath += named("main").get().output
                }
            }
        }

        dependencies {
            add("minecraft", "com.mojang:minecraft:$minecraftVersion")
            add("mappings", "net.fabricmc:yarn:$minecraftVersion+build.$yarnVersion:v2")

            add("modImplementation", "net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")

            project.findProperty("fabric_api_version")?.also {
                add("modImplementation", "net.fabricmc.fabric-api:fabric-api:$it")
            }
        }

        project.extensions.getByType(net.fabricmc.loom.api.LoomGradleExtensionAPI::class).apply {
            runs {
                named("client") {
                    ideConfigGenerated(false)
                }
                named("server") {
                    ideConfigGenerated(false)
                    serverWithGui()
                }

                if (projectUsesDatagen) {
                    create("datagen") {
                        client()
                        vmArg("-Dfabric-api.datagen")
                        vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
                        vmArg("-Dfabric-api.datagen.datagen.modid=${project.property("mod_id")}")
                        runDir("build/fabric-datagen")
                        source(javaPluginExtension.sourceSets.getByName("datagen"))
                    }
                }
            }

            mixin {
                useLegacyMixinAp.set(false)
            }

            project.findProperty("access_widener_path")?.also {
                accessWidenerPath.set(file("$it"))
            }
        }

        tasks.withType<ProcessResources> {
            val props = mutableMapOf("version" to properties["mod_version"]) // Needs to be mutable
            inputs.properties(props)
            filesMatching("fabric.mod.json") {
                expand(props)
            }
            exclude(".cache/*")
        }
    }
}

// Template above, do not edit.
