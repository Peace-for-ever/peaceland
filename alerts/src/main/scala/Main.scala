package alerts

import java.io.FileReader

import org.apache.spark.sql._
import models._
import org.apache.spark._
import org.apache.spark.sql.types._
import com.google.gson.Gson
import sttp.client3._
import org.apache.hadoop.fs.DF
import core.Utils

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
  val url = Utils.getTelegramUrl(sys.env("telegramBotToken"))

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
    data
      .collect()
      .map(row => row.getString(0))
      .map(x => {
        val msg = message.apply(
          sys.env("telegramChatID"),
          gson.fromJson(x, classOf[Event]).toAlert()
        )
        basicRequest
        .contentType("application/json")
        .body(gson.toJson(msg))
        .post(uri"$url")
        .send(backend)
      })
  }

}
