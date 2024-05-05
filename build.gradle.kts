plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id ("org.openjfx.javafxplugin") version "0.1.0"
    application
    `maven-publish`
}

group = "com.stmarygate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    kotlin.run {
        val envFile = file(".env")
        envFile.readLines().forEach {
            if (it.isNotEmpty() && !it.startsWith("#")) {
                val pos = it.indexOf("=")
                val key = it.substring(0, pos)
                val value = it.substring(pos + 1)
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value)
                }
            }
        }
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/St-Mary/Coral")
        credentials {
            username = System.getProperty("GITHUB_ACTOR").toString()
            password = System.getProperty("GITHUB_TOKEN").toString()
        }
    }
}

// Required by the 'shadowJar' task
project.setProperty("mainClassName", "com.stmarygate.cassandra.Cassandra")

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("io.github.cdimascio", "java-dotenv", "5.1.1")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.netty:netty-all:4.1.101.Final")
    implementation("org.springframework.security:spring-security-core:6.2.1")
    implementation("org.springframework.security:spring-security-crypto:6.2.1")
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")
    implementation("org.jline:jline-reader:3.25.0")
    implementation("org.jline:jline-terminal:3.25.0")

    implementation("org.openjfx:javafx-fxml:23-ea+3")
    implementation("org.openjfx:javafx-controls:23-ea+3")

    // SQLite database HikariCP and Hibernate
    implementation("org.xerial:sqlite-jdbc:3.45.3.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.1.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.1.Final")

    // implementation("com.stmarygate:coral:1.0.16")
    implementation(files("/Users/noelle/Desktop/Developpement/Projets/StMary-Gate/coral/build" +
            "/libs/coral-1.0.18.jar"))
}



tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "com.stmarygate.cassandra.Cassandra"
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("cassandra")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.test {
    useJUnitPlatform()
}