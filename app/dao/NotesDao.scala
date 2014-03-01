package dao

import play.modules.reactivemongo.json.collection.JSONCollection

import play.api.libs.json.Json
import models.Note
import scala.concurrent.Future
import reactivemongo.core.commands.LastError


class NotesDao extends Dao {

  lazy val collection = db[JSONCollection]("notes")


  def findAll(): Future[List[Note]] = {
    collection.find(Json.obj()).cursor[Note].collect[List]()
  }
  
  def insert(note: Note): Future[LastError] = {
    collection.insert(note)
  }

  def findById(id: String): Future[Option[Note]] = {
    collection.find(Json.obj("_id" -> Json.obj("$oid" ->id))).one[Note]
  }

}