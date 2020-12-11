package core

import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId

object Utils {
	val GRPC_URL = "https://peaceland.thomaslacaze.fr"
	val GRPC_PORT = 80
	val TELEGRAM_URL = "https://api.telegram.org/bot"
	val TELEGRAM_MESSAGE_ENDPOINT = "/sendMessage"

	def getTelegramUrl(botToken: String): String = {
		s"$TELEGRAM_URL$botToken$TELEGRAM_URL"
	}

	def strToDate(str: String): LocalDateTime = {
		val tmp = Instant.parse(str)
		LocalDateTime.ofInstant(tmp, ZoneId.systemDefault())
	}
}
