package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class Note(_id: Option[BSONObjectID], wine: Wine, note: String)

object Note {

  implicit val noteFormat = Json.format[Note]

}