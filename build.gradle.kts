import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer
import java.util.*

plugins {
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("eclipse")
    id("groovy")
    id("com.gtnewhorizons.retrofuturagradle") version "1.3.19"
}

// Project properties
group = "alecsio.modularmachineryaddons"
version = "1.0.3"

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
        vendor.set(JvmVendorSpec.AZUL)
    }
    // Generate sources and javadocs jars when building and publishing
    withSourcesJar()
    withJavadocJar()
}

// Most RFG configuration lives here, see the JavaDoc for com.gtnewhorizons.retrofuturagradle.MinecraftExtension
minecraft {
    mcVersion.set("1.12.2")

    // Username for client run configurations
    username.set("Dev")

    // Generate a field named VERSION with the mod version in the injected Tags class
    injectedTags.put("VERSION", project.version)

    // If you need the old replaceIn mechanism, prefer the injectTags task because it doesn't inject a javac plugin.
    // tagReplacementFiles.add("RfgExampleMod.java")

    // Enable assertions in the mod's package when running the client or server
    val args = mutableListOf("-ea:${project.group}")

    // Mixin args
    //args.add("-Dfml.coreMods.load=github.kasuminova.mmce.mixin.MMCEEarlyMixinLoader")
    args.add("-Dmixin.hotSwap=true")
    args.add("-Dmixin.checks.interfaces=true")
    args.add("-Dmixin.debug.export=true")
    extraRunJvmArguments.addAll(args)

    // If needed, add extra tweaker classes like for mixins.
    // extraTweakClasses.add("org.spongepowered.asm.launch.MixinTweaker")

    // Exclude some Maven dependency groups from being automatically included in the reobfuscated runs
    //groupsToExcludeFromAutoReobfMapping.addAll("com.diffplug", "com.diffplug.durian", "net.industrial-craft")
}

// Put the version from gradle into mcmod.info
tasks.processResources.configure {
    inputs.property("version", project.version)
    inputs.property("mcversion", minecraft.mcVersion.get())

    filesMatching("mcmod.info") {
        expand(mapOf("version" to project.version, "mcversion" to minecraft.mcVersion.get()))
    }
}

tasks.compileJava.configure {
    sourceCompatibility = "17"
    options.release = 8
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation

    javaCompiler = javaToolchains.compilerFor {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar.configure {
    manifest {
        val attributes = manifest.attributes
        //attributes["FMLCorePlugin"] = "github.kasuminova.mmce.mixin.MMCEEarlyMixinLoader"
        attributes["FMLCorePluginContainsFMLMod"] = true
    }
}

tasks.javadoc.configure {
    // No need for JavaDoc.
    actions = Collections.emptyList()
}

tasks.test {
    useJUnitPlatform() // Required for Spock 2.x
}

// Create a new dependency type for runtime-only dependencies that don't get included in the maven publication
val runtimeOnlyNonPublishable: Configuration by configurations.creating {
    description = "Runtime only dependencies that are not published alongside the jar"
    isCanBeConsumed = false
    isCanBeResolved = false
}
listOf(configurations.runtimeClasspath, configurations.testRuntimeClasspath).forEach {
    it.configure {
        extendsFrom(
                runtimeOnlyNonPublishable
        )
    }
}

// Add an access tranformer
// tasks.deobfuscateMergedJarToSrg.configure {accessTransformerFiles.from("src/main/resources/META-INF/mymod_at.cfg")}

// Dependencies
repositories {
    flatDir {
        dirs("libs")
    }
    maven {
        url = uri("https://maven.aliyun.com/nexus/content/groups/public/")
    }
    maven {
        url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")
    }
    maven {
        url = uri("https://maven.cleanroommc.com")
    }
    maven {
        url = uri("https://cfa2.cursemaven.com")
    }
    maven {
        url = uri("https://cursemaven.com")
    }
    maven {
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven {
        name = "OvermindDL1 Maven"
        url = uri("https://gregtech.overminddl1.com/")
        mavenContent {
            excludeGroup("net.minecraftforge") // missing the `universal` artefact
        }
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    }
    maven {
        name = "GTNH Maven"
        url = uri("http://jenkins.usrv.eu:8081/nexus/content/groups/public/")
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

dependencies {
    annotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:0.4.2")
    compileOnly("com.github.bsideup.jabel:jabel-javac-plugin:0.4.2")
    // workaround for https://github.com/bsideup/jabel/issues/174
    annotationProcessor("net.java.dev.jna:jna-platform:5.13.0")
    // Allow jdk.unsupported classes like sun.misc.Unsafe, workaround for JDK-8206937 and fixes Forge crashes in tests.
    patchedMinecraft("me.eigenraven.java8unsupported:java-8-unsupported-shim:1.0.0")
    // allow Jabel to work in tests
    testAnnotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0")
    testImplementation ("org.codehaus.groovy:groovy-all:3.0.9")  // Latest Groovy version


    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    testCompileOnly("com.github.bsideup.jabel:jabel-javac-plugin:1.0.0") {
        isTransitive = false // We only care about the 1 annotation class
    }
    testCompileOnly("me.eigenraven.java8unsupported:java-8-unsupported-shim:1.0.0")
    implementation("zone.rong:mixinbooter:8.9")
    // Mixins
    val mixin : String = modUtils.enableMixins("zone.rong:mixinbooter:8.9", "mixins.mmcea.refmap.json").toString()
    api (mixin) {
        isTransitive = false
    }

    annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
    annotationProcessor("com.google.guava:guava:30.0-jre")
    annotationProcessor("com.google.code.gson:gson:2.8.9")

    annotationProcessor (mixin) {
        isTransitive = false
    }

    implementation(rfg.deobf("curse.maven:ae2-570458:6302098-sources-6302100"))
    implementation(rfg.deobf("curse.maven:ModularMachineryCE-817377:5527355-sources-5527359"))
    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.+")
    implementation("curse.maven:thaumicEnergistics-223666:2915506")
    implementation("curse.maven:thaum-223628:2629023")
    implementation("curse.maven:baubles-227083:2518667")
    implementation("curse.maven:projecte-226410:2702991")
    implementation("curse.maven:worldedit-225608:2941712")
    implementation("curse.maven:projectx-311378:3014173")
    implementation(rfg.deobf("curse.maven:spark-361579:3542217"))

    implementation(rfg.deobf("curse.maven:thaumicaug-319441:6047659"))

    implementation(rfg.deobf("curse.maven:contentTweaker-237065:3331364"))
    implementation(rfg.deobf("curse.maven:contentTweakerDependency-246996:3440963"))
    implementation(rfg.deobf("curse.maven:mixinbootstrap-357178:3437402"))
    implementation(rfg.deobf("curse.maven:had-enough-items-557549:4810661"))
    implementation(rfg.deobf("curse.maven:CodeChickenLib-242818:2779848"))
    implementation(rfg.deobf("curse.maven:the-one-probe-245211:2667280"))
    implementation(rfg.deobf("curse.maven:rftools-224641:2861573"))
    implementation(rfg.deobf("curse.maven:rftools-dep-mcjtylib-233105:2745846"))
    implementation(rfg.deobf("curse.maven:trash-394535:4606884"))
    implementation(rfg.deobf("curse.maven:trashdep-454372:6034694"))
    implementation("curse.maven:nuclearcraft-226254:6151363-sources-6151372")
    implementation(rfg.deobf("curse.maven:drawers-223852:5981297"))
    implementation(rfg.deobf("curse.maven:drawersChameleonDependency-230497:2450900"))
    implementation(rfg.deobf("curse.maven:chisel-278493:3319307"))
    implementation("curse.maven:bloodmagic-224791:2822288-sources-2822290")
    implementation(rfg.deobf("curse.maven:bmdependency-228832:2645992"))
    compileOnly(rfg.deobf("curse.maven:mantle-74924:2713386"))
    implementation(rfg.deobf("curse.maven:tx-loader-706505:4515357"))

    // GeckoLib
    implementation("software.bernie.geckolib:geckolib-forge-1.12.2:3.0.31")
}

// Publishing to a Maven repository
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
//        // Example: publishing to the GTNH Maven repository
//        maven {
//            url = uri("http://jenkins.usrv.eu:8081/nexus/content/repositories/releases")
//            isAllowInsecureProtocol = true
//            credentials {
//                username = System.getenv("MAVEN_USER") ?: "NONE"
//                password = System.getenv("MAVEN_PASSWORD") ?: "NONE"
//            }
//        }
    }
}

// IDE Settings
//eclipse {
//    classpath {
//        isDownloadSources = true
//        isDownloadJavadoc = true
//    }
//}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        inheritOutputDirs = true // Fix resources in IJ-Native runs
    }
    project {
        this.withGroovyBuilder {
            "settings" {
                "runConfigurations" {
                    val self = this.delegate as RunConfigurationContainer
                    self.add(Gradle("1. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    })
                    self.add(Gradle("2. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    })
                    self.add(Gradle("3. Run Obfuscated Client").apply {
                        setProperty("taskNames", listOf("runObfClient"))
                    })
                    self.add(Gradle("4. Run Obfuscated Server").apply {
                        setProperty("taskNames", listOf("runObfServer"))
                    })
                    /*
                    These require extra configuration in IntelliJ, so are not enabled by default
                    self.add(Application("Run Client (IJ Native, Deprecated)", project).apply {
                      mainClass = "GradleStart"
                      moduleName = project.name + ".ideVirtualMain"
                      afterEvaluate {
                        val runClient = tasks.runClient.get()
                        workingDirectory = runClient.workingDir.absolutePath
                        programParameters = runClient.calculateArgs(project).map { '"' + it + '"' }.joinToString(" ")
                        jvmArgs = runClient.calculateJvmArgs(project).map { '"' + it + '"' }.joinToString(" ") +
                          ' ' + runClient.systemProperties.map { "\"-D" + it.key + '=' + it.value.toString() + '"' }
                          .joinToString(" ")
                      }
                    })
                    self.add(Application("Run Server (IJ Native, Deprecated)", project).apply {
                      mainClass = "GradleStartServer"
                      moduleName = project.name + ".ideVirtualMain"
                      afterEvaluate {
                        val runServer = tasks.runServer.get()
                        workingDirectory = runServer.workingDir.absolutePath
                        programParameters = runServer.calculateArgs(project).map { '"' + it + '"' }.joinToString(" ")
                        jvmArgs = runServer.calculateJvmArgs(project).map { '"' + it + '"' }.joinToString(" ") +
                          ' ' + runServer.systemProperties.map { "\"-D" + it.key + '=' + it.value.toString() + '"' }
                          .joinToString(" ")
                      }
                    })
                    */
                }
                "compiler" {
                    val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
                    afterEvaluate {
                        self.javac.moduleJavacAdditionalOptions = mapOf(
                                (project.name + ".main") to
                                        tasks.compileJava.get().options.compilerArgs.map { '"' + it + '"' }.joinToString(" ")
                        )
                    }
                }
            }
        }
    }
}

tasks.processIdeaSettings.configure {
    dependsOn(tasks.injectTags)
}