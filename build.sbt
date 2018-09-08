import sbt._

/**
  * Default Scala version for all
  */
scalaVersion := "2.12.6"

/**
  * -==================================================-
  * COMMON SETTINGS
  * -==================================================-
  */
lazy val commonSettings = Seq(
  organization := "it.giorgio",
  version := "0.0.1-SNAPSHOT",
)

lazy val test-example-scala = (project in file("."))
  .settings(
    name := "test-example-scala",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.0.5" % "test",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.mockito" % "mockito-all" % "1.10.19"
    )
)



