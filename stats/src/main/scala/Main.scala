package stats

import org.apache.spark._
import com.arangodb.spark.ArangoSpark
import com.arangodb.spark.ReadOptions
import models.Event
import core.Utils

object Main {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
			.setAppName("Stats")
			.setMaster("local[*]")
			.set("arangodb.host", "localhost")
    		.set("arangodb.port", "8529")
			.set("arangodb.user", "root")
			.set("arangodb.password", "scala")
		
		val sc = new SparkContext(conf)

		val result = ArangoSpark.load[Event](sc, "records")
		val count = result.count()
		
		println("Question 1:")
		val partDay = result.groupBy(x => {
			val hour = Utils.timestampToDate(x.date).getHour()
			if(hour >= 0 && hour < 12) "morning" else "evening"
		}).sortBy(x => x._2.size, ascending = false)
		//partDay.foreach(x => println(s"${x._1} : ${x._2.size}"))
		val angryHour = partDay.first()
		println(s"The most pissed off people are in the ${angryHour._1} : ${angryHour._2.size}")

		println("Question 2:")
		val country = result.groupBy(_.country).sortBy(x => x._2.size, ascending = false)
		//country.foreach(x => println(s"${x._1} : ${x._2.size}"))
		println(s"Countries : ${country.count()}")
		val angry = country.first()
		println(s"Country with the most pissed off people: ${angry._1} : ${angry._2.size}")

		println("Question 3:")

		println("Question 4:")
		val weekDays = result.groupBy(x => Utils.strToDate(x.date).getDayOfWeek()).sortBy(x => x._2.size, ascending = false)
		val weekAngry = weekDays.first()
		//weekDays.foreach(x => println(s"${x._1} : ${x._2.size}"))
		println(s"Day of the week with the most pissed off people: ${weekAngry._1} : ${weekAngry._2.size}")

		println("Question 5:")
		val percent = result.filter(_.battery > 70).count() * 100 / count
		println(s"There is $percent% of drones with more than 70% of battery")
	}
}