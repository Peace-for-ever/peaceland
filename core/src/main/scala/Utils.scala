package core

object Utils {
	val API_URL = "https://peaceland.thomaslacaze.fr"
	val TELEGRAM_URL = "https://api.telegram.org/bot"+sys.env("telegramBotToken")+"/sendMessage"
}
