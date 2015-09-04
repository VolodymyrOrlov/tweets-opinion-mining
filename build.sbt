name := "tweets-opinion-mining"

organization := "com.vorlov"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

val sprayVersion = "1.3.+"

val akkaVersion = "2.3.+"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "io.spray" %% "spray-http" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
  "io.spray" %% "spray-json" % sprayVersion,
  "io.spray" %% "spray-can" % sprayVersion,
  "joda-time" % "joda-time" % "2.8.1",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "org.scalanlp" % "breeze_2.10" % "0.11.2",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.scalaz" %% "scalaz-core" % "7.1.3",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

mainClass in Compile := Some("com.vorlov.Main")