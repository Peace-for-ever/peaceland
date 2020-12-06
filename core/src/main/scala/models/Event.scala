package models

case class Event(
    val citizen: String,
    val message: String,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val battery: Int,
    val temperature: Int
) {
  def toAlert(): String = {
    return  "Batterie: " + battery+ "\nLocalisation: " + longitude+","+latitude+ "\nTempérature: " + temperature;
  }
}
