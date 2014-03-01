package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.Note
import models.Note.noteFormat

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import dao.NotesDao

class NotesController(notesDao: NotesDao) extends Controller {

  def list() = Action.async { implicit request =>
    notesDao.findAll().map {
      notes => Ok(Json.toJson(notes))
    }
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[Note].map { note =>
      notesDao.insert(note).map { lastError =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def show(id: String) = Action.async(parse.empty) { request =>
    notesDao.findById(id).map { f =>
      Ok(Json.toJson(f.get))
    }
  }

}
