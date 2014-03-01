package models

import play.api.libs.json.Json

case class Wine(name: String, vintage: String)

object Wine {

  implicit val wineFormat = Json.format[Wine]

}