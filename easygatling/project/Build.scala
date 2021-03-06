import sbt._
import Keys._
import com.typesafe.startscript.StartScriptPlugin
import cc.spray.revolver.RevolverPlugin._
import classpath.ClasspathUtilities.isArchive
import java.io.FileOutputStream
import sbtassembly.Plugin._
import AssemblyKeys._

object BuildSettings {
  import Dependencies._

  val buildOrganization = "com.sillycat.easygatling"
  val buildVersion = "0.1"
  val buildScalaVersion = "2.9.3"

  val globalSettings = Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    javacOptions := Seq("-Xlint:unchecked", "-Xlint:deprecation","-encoding", "utf8"),
    fork in test := true,
    resolvers ++= Dependencies.resolutionRepos)
  val projectSettings = Defaults.defaultSettings ++ globalSettings
}

object Build extends sbt.Build {
  import Dependencies._
  import BuildSettings._

  override lazy val settings = super.settings ++ globalSettings

  lazy val gatlingTemplate = Project("gatling-sbt",
    file("."),
    settings = projectSettings ++ assemblySettings ++
      Revolver.settings ++
      StartScriptPlugin.startScriptForJarSettings ++
      Seq(libraryDependencies ++= Seq(
        Compile.logback,
        Compile.gatlingApp,
        Compile.gatlingRecorder,
        Compile.gatlingHighCharts
      )))
}

object Dependencies {

  val resolutionRepos = Seq(
    "Scala Tools" at "http://scala-tools.org/repo-releases/",
    "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases","Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    "Excilys" at "http://repository.excilys.com/content/groups/public",
    "Excilys Snapshot" at "http://repository-gatling.forge.cloudbees.com/snapshot",
    "spray repo" at "http://repo.spray.io",
    "spray nightlies repo" at "http://nightlies.spray.io" )

  object V {
    val slf4j = "1.7.2"
    val logback = "1.0.0"
    val gatling = "1.5.3"
    //val gatling = "2.0.0-SNAPSHOT"
  }

  object Compile {
    val gatlingApp = "com.excilys.ebi.gatling" % "gatling-app" % V.gatling % "compile"
    val gatlingRecorder = "com.excilys.ebi.gatling" % "gatling-recorder" % V.gatling % "compile"
    val gatlingHighCharts = "com.excilys.ebi.gatling.highcharts" % "gatling-charts-highcharts" % V.gatling % "compile"
    val logback = "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime"
    val log4j = "log4j" % "log4j" % "1.2.14" % "compile"
  }
}
