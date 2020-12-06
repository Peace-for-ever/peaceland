package producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
object Producer {

	def heartbeat(message: String, producer: KafkaProducer[String,String], topic: String): Unit = {
		val record = new ProducerRecord[String, String](topic, message.hashCode.toString, message)

		producer.send(record)
	}
}
