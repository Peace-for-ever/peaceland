package models

case class Event(
	var citizen: String,
	var message: String,
	var latitude: Double,
	var longitude: Double,
	var date: String,
	var battery: Int,
	var temperature: Int
)
