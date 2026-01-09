import xerial.sbt.Sonatype._

lazy val scala2 = "2.13.18"
lazy val scala33 = "3.3.7"
lazy val scala37 = "3.7.3"
lazy val supportedScalaVersions = List(scala2, scala33,scala37)

lazy val commonSettings = Seq(
  scalaVersion := scala37,
  libraryDependencies ++= Dependencies.all
)

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin
  )
  .settings(
    commonSettings,
    name := "slogic",
    scalacOptions ++= Scalac.options,
    crossScalaVersions := supportedScalaVersions
  )
ThisBuild / version := "0.3.2"
ThisBuild / organization := "io.github.thediscprog"
ThisBuild / organizationName := "thediscprog"
ThisBuild / organizationHomepage := Some(url("https://github.com/TheDiscProg"))

ThisBuild / description := "Scala Logic Library."

// Sonatype/Maven Publishing
ThisBuild / publishMavenStyle := true
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeProfileName := "io.github.thediscprog"
ThisBuild / publishMavenStyle := true
ThisBuild / licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/thediscprog/slogic"))
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("TheDiscProg", "slogic", "TheDiscProg@gmail.com"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/thediscprog/slogic"),
    "scm:git@github.com:thediscprog/slogic.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "thediscprog",
    name = "TheDiscProg",
    email = "TheDiscProg@gmail.com",
    url = url("https://github.com/TheDiscProg")
  )
)

sonatypeCredentialHost := "central.sonatype.com"
sonatypeRepository := "https://central.sonatype.com/api/v1/publisher/"

ThisBuild / versionScheme := Some("early-semver")

// scalafmt will make changes in Xor.scala that breaks the code
addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")