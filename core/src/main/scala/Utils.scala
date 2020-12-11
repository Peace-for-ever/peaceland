package core

import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId

object Utils {
	val GRPC_URL = "peaceland.thomaslacaze.fr"
	val GRPC_PORT = 8083
	val TELEGRAM_URL = "https://api.telegram.org/bot"
	val TELEGRAM_MESSAGE_ENDPOINT = "/sendMessage"

	def getTelegramUrl(botToken: String): String = {
		s"$TELEGRAM_URL$botToken$TELEGRAM_URL"
	}

	def timestampToDate(ts: Long): LocalDateTime = {
		val tmp = Instant.ofEpochSecond(ts)
		LocalDateTime.ofInstant(tmp, ZoneId.systemDefault())
	}
}
