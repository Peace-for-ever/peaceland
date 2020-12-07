package core

import sttp.client3._
import io.circe.parser._
import com.google.gson.Gson
import models._

object Arango {

	def createCollection(name: String): Unit = {
		val auth = new Auth(sys.env("arangoUser"), sys.env("arangoPassword"))
		val gson = new Gson
		val body = gson.toJson(auth)
		val request = basicRequest
			.body(body)
			.post(uri"http://${sys.env("arangoHost")}/_open/auth")
			.response(asString.getRight)
		
		val backend = HttpURLConnectionBackend()
		val response = request.send(backend)

		val collection = new Collection(name)
		val collectionBody = gson.toJson(collection)
		val jwt = gson.fromJson(response.body, classOf[Jwt])
		val requestCollection = basicRequest
			.auth.bearer(jwt.jwt)
			.body(collectionBody)
			.post(uri"http://${sys.env("arangoHost")}/_api/collection")
			.response(asString.getRight)

		try {
			requestCollection.send(backend)
		}
		catch {
			case e: Exception => None
		}
	}
}