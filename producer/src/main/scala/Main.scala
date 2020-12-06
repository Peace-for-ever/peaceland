package producer

import Producer._
import java.io.FileReader
import core.Csv._
import core.Api._
import scala.io._
import models.Config
import com.google.gson.Gson
import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer

object Main {
  def main(args: Array[String]): Unit = {

    val cfg: Config = new Config(
      sys.env("kafkaHost"),
      sys.env("kafkaPort"),
      sys.env("kafkaRecordTopic"),
      sys.env("csvPath"),
      sys.env("kafkaAlertTopic"),
      sys.env("csvPathAlert")
    )

    val events = generateEvents(100);

    val gson = new Gson

    val topic = cfg.kafkaRecordTopic
    val alert = cfg.kafkaAlertTopic
    val props = new Properties()
    props.put(
      "bootstrap.servers",
      String.format("%s:%s", cfg.kafkaHost, cfg.kafkaPort)
    )
    props.put(
      "key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    props.put(
      "value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )

    val producer = new KafkaProducer[String, String](props)

    events.head.map(e => Producer.heartbeat(gson.toJson(e), producer, topic))
    events.head.filter(e => e.battery <= 10).map(e => Producer.heartbeat(gson.toJson(e), producer, alert))

    writeCsv(cfg.csvPathAlert, events.head.filter(e => e.battery <= 10))
    writeCsv(cfg.csvPath, events.head)

    producer.close()

  }
}
