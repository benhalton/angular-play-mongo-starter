package scala.integration

import play.api.libs.ws.{Response, WS}

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import models.{Wine, Note}
import scala.concurrent.{ExecutionContext, Future}
import play.Logger
import ExecutionContext.Implicits.global
import reactivemongo.bson.BSONObjectID

class NotesControllerIntegrationSpec extends PlaySpecification with EmbeddedMongo {

  val createNoteUrl = "http://localhost:19001/api/notes"
  val getNoteUrl = "http://localhost:19001/api/notes/"
  val listNotesUrl = "http://localhost:19001/api/notes"

  "test create and read Note" in new WithServer(new FakeApplication(additionalConfiguration = inMemoryMongoDatabase())) {

    val id: BSONObjectID = BSONObjectID.generate
    Logger.warn(id.stringify)
    val note = Note(Some(id), Wine("foo", "1990"), "notey notey very notey")

    val response: Response = await(WS.url(createNoteUrl).post(Json.toJson(note)))
    Logger.warn(response.toString)
    response.status must beEqualTo(CREATED)

    val response2: Response = await(WS.url(listNotesUrl).get)
    Logger.warn(Json.stringify(response2.json))
    response2.status must beEqualTo(OK)

    val foundNote: Response = await(WS.url(getNoteUrl + id.stringify).get)
    foundNote.status must beEqualTo(OK)
    Logger.warn(Json.stringify(foundNote.json))

  }

}