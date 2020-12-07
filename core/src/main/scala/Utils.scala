package core

object Utils {
	val API_URL = "https://peaceland.thomaslacaze.fr"
	val TELEGRAM_URL = "https://api.telegram.org/bot"
	val TELEGRAM_MESSAGE_ENDPOINT = "/sendMessage"

	def getTelegramUrl(botToken: String): String = {
		s"$TELEGRAM_URL$botToken$TELEGRAM_URL"
	}
}
