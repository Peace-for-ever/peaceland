name := "peaceland"
scalaVersion in ThisBuild := "2.12.12"

// PROJECTS

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sttp,
      dependencies.circe,
      dependencies.kantan
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val producer = (project in file("producer"))
  .settings(
    name := "producer",
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.kafka,
      dependencies.log4j
    )
  )
  .dependsOn(
    core
  )

lazy val storage = (project in file("storage"))
  .settings(
    name := "storage",
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


lazy val alerts = (project in file("alerts"))
  .settings(
    name := "alerts",
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sparkCore,
      dependencies.sparkSQL,
      dependencies.sparkKafka,
      dependencies.sttp
    )
  )
  .dependsOn(
    core
  )

lazy val stats = (project in file("stats"))
  .settings(
    name := "stats",
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
    val telegramV = "0.5.1"

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
    val telegram = "org.augustjune" %% "canoe" % telegramV
  }

lazy val commonDependencies = Seq(
  dependencies.gson
)