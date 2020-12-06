package core

import java.io.File

import Api.generateEvents
import models._
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._ // Automatic derivation of codecs.

object Csv {

	def writeCsv(path: String, events: List[Event]): Unit = {
		new File(path).writeCsv[Event](events, rfc)
	}

	def readCsv(path: String): List[Event] = {
		new File(path).readCsv[List, Event](rfc).map(x => x.toOption.head)
	}
}
