/**
 *    Copyright 2011-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := "org.mybatis.scala",
    version      := "1.0.7",
    scalaVersion := "2.12.2"
  )
  val mybatisVersion = "3.2.8"
}

object Resolvers {
  val sonatypeSnapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatype = "Sonatype OSS releases" at "http://oss.sonatype.org/content/repositories/releases"
}

object Dependencies {
  val mybatis = "org.mybatis.scala" %% "mybatis-scala-core" % "1.0.7"
  val mysqldb = "mysql" % "mysql-connector-java" % "8.0.11"
  val scalaxml = "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.7"
}

object MainBuild extends Build {

  import BuildSettings._
  import Resolvers._
  import Dependencies._

  val deps = Seq(mybatis, scalaxml, mysqldb)
  val coreResolvers = Seq(sonatypeSnapshots, sonatype)

  lazy val samples = Project(
    "mybatis-scala-samples",
    file("."),
    settings = buildSettings ++ Seq(resolvers := coreResolvers, libraryDependencies ++= deps)
  )

}
