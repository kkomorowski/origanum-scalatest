ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file(".")).settings(name := "origanum-scalatest")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.5"

Test / parallelExecution := false
Test / fork := false
Test / testOptions += Tests.Argument("-C", "io.github.kkomorowski.origanum.scalatest.OriganumReporter")