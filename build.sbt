import xerial.sbt.Sonatype.sonatypeCentralHost

lazy val scala2 = "2.13.14"
lazy val scala3 = "3.5.1"
lazy val supportedScalaVersions = List(scala2, scala3)

lazy val commonSettings = Seq(
  scalaVersion := scala3,
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

// scalafmt will make changes in Xor.scala that breaks the code
addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")

// Sonatype/Maven Publishing
inThisBuild(
  List(
    organization := "io.github.thediscprog",
    homepage := Some(url("https://github.com/TheDiscProg/slogic")),
    licenses := List("GNU-3.0" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html")),
    developers := List(
      Developer(
        "thediscprog",
        "TheDiscProg",
        "TheDiscProg@gmail.com",
        url("https://github.com/TheDiscProg")
      )
    )
  )
)

ThisBuild / sonatypeCredentialHost := sonatypeCentralHost
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
publishTo := sonatypePublishToBundle.value
