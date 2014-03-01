package injection

import org.springframework.scala.context.function.FunctionalConfiguration
import dao.{UserDao, NotesDao}
import controllers._


class AppConfiguration extends FunctionalConfiguration {

  val notesDao = bean("notesDao") {
    new NotesDao
  }

  val userDao = bean("userDao") {
    new UserDao
  }

  bean("notesController") {
    new NotesController(notesDao())
  }

  bean("applicationController") {
    new Application(userDao())
  }

}