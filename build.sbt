organization in ThisBuild := "com.kevin"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.1"

val scalaTest = "org.scalatest" %% "scalatest" % "3.1.0" % Test
val macwire = "com.softwaremill.macwire"  %% "macros" % "2.3.3" % "provided"

lazy val `lagom-blog-api` = (project in file("."))
  .aggregate(`blog-api`, `blog-impl`)

lazy val `blog-api` = (project in file("blog-api"))
  .enablePlugins(ScalafmtPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `blog-impl` = (project in file("blog-impl"))
  .enablePlugins(LagomScala, ScalafmtPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslPersistenceJdbc,
      lagomScaladslTestKit,
      scalaTest,
      macwire,
      "mysql" % "mysql-connector-java" % "6.0.6",
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`blog-api`)

scalacOptions in ThisBuild := Seq(
  "-unchecked",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Xlint"
)

/*
 * If you set this to true, make sure to also clear the read side
 * before you restart the application
 * (e.g. by doing a "truncate league" in the league schema).
 */
lagomCassandraCleanOnStart in ThisBuild := false
