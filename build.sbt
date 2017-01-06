name := "DouyuTvScraper"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.8"

// https://mvnrepository.com/artifact/com.ning/async-http-client
libraryDependencies += "com.ning" % "async-http-client" % "1.9.39"

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.1.2"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

libraryDependencies += "com.ning" % "async-http-client" % "1.9.31"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http-core" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % akkaVersion

libraryDependencies += "io.reactivex" % "rxscala_2.11" % "0.26.2"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.1"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.1"

libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0-M11" % "test"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "1.7.2"