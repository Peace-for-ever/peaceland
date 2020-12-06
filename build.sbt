name := "peaceland"
scalaVersion in ThisBuild := "2.12.12"

// PROJECTS

lazy val globalResources = file("resources")

lazy val global = project
  .in(file("."))
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    core,
    producer,
    storage,
    alerts,
    stats
  )

lazy val core = project
  .settings(
    name := "core",
    unmanagedResourceDirectories in Runtime += globalResources,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sttp,
      dependencies.circe,
      dependencies.kantan
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val producer = project
  .settings(
    name := "producer",
    unmanagedResourceDirectories in Runtime += globalResources,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.kafka,
      dependencies.log4j
    )
  )
  .dependsOn(
    core
  )

lazy val storage = project
  .settings(
    name := "storage",
    unmanagedResourceDirectories in Runtime += globalResources,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sparkCore,
      dependencies.sparkSQL,
      dependencies.sparkKafka,
      dependencies.arangodb,
    )
  )
  .dependsOn(
    core
  )


lazy val alerts = project
  .settings(
    name := "alerts",
    unmanagedResourceDirectories in Runtime += globalResources,
    libraryDependencies ++= commonDependencies ++ Seq(
      
    )
  )
  .dependsOn(
    core
  )

lazy val stats = project
  .settings(
    name := "stats",
    unmanagedResourceDirectories in Runtime += globalResources,
    libraryDependencies ++= commonDependencies ++ Seq(
      
    )
  )
  .dependsOn(
    core
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val sttpV = "3.0.0-RC11"
    val circeV = "0.12.3"
    val kantanV = "0.6.1"
    val kafkaV = "2.6.0"
    val log4jV = "1.7.25"
    val gsonV = "2.8.6"
    val sparkV = "3.0.1"
    val arangodbV = "1.1.0"

    val sttp = "com.softwaremill.sttp.client3" %% "core" % sttpV
    val circe = "io.circe" %% "circe-parser" % circeV
    val kantan = "com.nrinaudo" %% "kantan.csv-generic" % kantanV

    val kafka = "org.apache.kafka" %% "kafka" % kafkaV
    val log4j = "org.slf4j" % "slf4j-log4j12" % log4jV
    val gson = "com.google.code.gson" % "gson" % gsonV

    val sparkCore = "org.apache.spark" %% "spark-core" % sparkV
    val sparkSQL = "org.apache.spark" %% "spark-sql" % sparkV
    val sparkKafka = "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkV

    val arangodb = "com.arangodb" %% "arangodb-spark-connector" % arangodbV
  }

lazy val commonDependencies = Seq(
  dependencies.gson
)
