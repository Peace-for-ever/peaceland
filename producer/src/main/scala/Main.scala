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
import models.Event

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

    val events = generateEvents(1000)

    val gson = new Gson

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

    val filteredEvents = getFilteredEvents(events.head)

    // count filtered events, usefull when scaling with docker compose
    println(s"Filtered events: ${filteredEvents.size}")

    filteredEvents.map(e => Producer.heartbeat(gson.toJson(e), producer, cfg.kafkaRecordTopic))
    
    val alerts = events.head.filter(e => e.battery <= 5)
    alerts.map(e => Producer.heartbeat(gson.toJson(e), producer, cfg.kafkaAlertTopic))

    writeCsv(cfg.csvPathAlert, alerts)
    writeCsv(cfg.csvPath, events.head)

    producer.close()

  }

  // get all events where there no word talking about the president Macrin Ping
  def getFilteredEvents(events: List[Event]): List[Event] = {
    val presidentWord = events
      .flatMap(x => x.message.replace('.', ' ').split(' '))
      .map(x => (x, 1))
      .groupBy(_._1)
      .mapValues(_.size)
      .maxBy(_._2)
      ._1

    println(s"President word: $presidentWord")
    
    events.filter(x => !(x.message.contains(presidentWord)))
  }
}
