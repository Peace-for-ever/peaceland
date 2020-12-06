package storage

import java.io.FileReader

import org.apache.spark.sql._
import models._
import org.apache.spark._
import com.arangodb.spark.ArangoSpark
import org.apache.spark.sql.types._
import com.google.gson.Gson
import com.arangodb.spark.WriteOptions

object Main {

	val spark: SparkSession = SparkSession
		.builder()
		.config("spark.master", "local")
		.getOrCreate()

	def main(args: Array[String]): Unit = {

		val cfg: Config = new Config(
			sys.env("kafkaHost"),
			sys.env("kafkaPort"),
			sys.env("kafkaRecordTopic"),
			null,
			null,
			null
		)
		startup(cfg)
	}

	def startup(cfg: Config): Unit = {

		spark.conf.set("arangodb.hosts", sys.env("arangoHost"))

		val kafkaStreamDF = spark.readStream
			.format("kafka")
			.option(
				"kafka.bootstrap.servers",
				String.format("%s:%s", cfg.kafkaHost, cfg.kafkaPort)
			)
			.option("subscribe", cfg.kafkaRecordTopic)
			.option("startingOffsets", "latest")
			.load()
			.selectExpr("CAST(value AS STRING)")

		val gson = new Gson
		val options = new WriteOptions()
			.user(sys.env("arangoUser"))
			.password(sys.env("arangoPassword"))

		val query = kafkaStreamDF.writeStream
			.foreachBatch { 
				(data: DataFrame, batchId: Long) =>
				val events = data.collect().map(row => row.getString(0)).map(x => gson.fromJson(x, classOf[Event]))
				val df = spark.createDataFrame(events)
				ArangoSpark.saveDF(df, "records", options)
			}
			.start()
		query.awaitTermination()

	}

}
