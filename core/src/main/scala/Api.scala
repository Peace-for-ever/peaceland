package core

import io.circe.Decoder
import sttp.client3._
import io.circe.parser._
import models.Event

object Api {

	implicit val eventDecoder: Decoder[Event] = Decoder.
		forProduct8("citizen", "message", "latitude", "longitude",
			"date", "battery", "temperature", "country")(Event.apply)

	def generateEvents(amount: Int): Option[List[Event]] = {
		val request = basicRequest
			.get(uri"${core.Utils.API_URL}/events?amount=$amount")
			.response(asString.getRight)

		val backend = HttpURLConnectionBackend()
		val response = request.send(backend)

		if (response.code.isSuccess)
			parse(response.body) match {
				case Left(error) => None
				case Right(json) => json.as[List[Event]] match {
					case Left(error) => None
					case Right(events) => Option(events)
				}
			}
		else
			None
	}
}
