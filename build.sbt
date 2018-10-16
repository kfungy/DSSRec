import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "DSSRec"

version := "0.0.1"

scalaVersion := "2.11.8"

description := "Provides recommendations"

parallelExecution in test := false

version in ThisBuild := sys.env.getOrElse("VERSION", default = "1.0.0-SNAPSHOT")

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

assemblyJarName in assembly := {
  name.value + "-" + version.value + ".jar"
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @_*) => MergeStrategy.rename
  case _ => MergeStrategy.first
}
