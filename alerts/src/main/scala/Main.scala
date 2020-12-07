package alerts

import java.io.FileReader

import org.apache.spark.sql._
import models._
import org.apache.spark._
import org.apache.spark.sql.types._
import com.google.gson.Gson
import sttp.client3._
import org.apache.hadoop.fs.DF

object Main {

  val spark: SparkSession = SparkSession
    .builder()
    .config("spark.master", "local")
    .getOrCreate()
  val cfg: Config = new Config(
    sys.env("kafkaHost"),
    sys.env("kafkaPort"),
    null,
    null,
    sys.env("kafkaAlertTopic"),
    null
  )

  val gson = new Gson
  val message = Message
  val backend = HttpURLConnectionBackend()

  def main(args: Array[String]): Unit = {
    val kafkaStreamDF = spark.readStream
      .format("kafka")
      .option(
        "kafka.bootstrap.servers",
        String.format("%s:%s", cfg.kafkaHost, cfg.kafkaPort)
      )
      .option("subscribe", cfg.kafkaAlertTopic)
      .option("startingOffsets", "latest")
      .load()
      .selectExpr("CAST(value AS STRING)")

    val query = kafkaStreamDF.writeStream
      .foreachBatch(DFProcessing _)
      .start()
    query.awaitTermination()
  }

  def DFProcessing(data: DataFrame, batchId: Long) {
    val events = data
      .collect()
      .map(row => row.getString(0))
      .map(x =>
        message.apply(
          sys.env("telegramChatID"),
          gson.fromJson(x, classOf[Event]).toAlert()
        )
      )

    events.foreach(x =>
      basicRequest
        .contentType("application/json")
        .body(gson.toJson(x))
        .post(uri"${core.Utils.TELEGRAM_URL}")
        .send(backend)
    )
  }

}
