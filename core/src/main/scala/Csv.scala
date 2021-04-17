package core

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

import Api.generateEvents
import models._
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._

object Csv {

	def writeCsv(path: String, events: List[Event]): Unit = {
		try {
			Files.createDirectories(Paths.get(path).getParent())
		}
		catch {
			case _: Throwable => println(path + " has no parent directories")
		}
		new File(path).writeCsv[Event](events, rfc)
	}

	def readCsv(path: String): List[Event] = {
		new File(path).readCsv[List, Event](rfc).map(x => x.toOption.head)
	}
}
