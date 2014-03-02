package injection

import org.springframework.scala.context.function.FunctionalConfiguration
import dao.{UserDao, NotesDao}
import controllers._
import service.UserService


class AppConfiguration extends FunctionalConfiguration {

  val notesDao = bean("notesDao") {
    new NotesDao
  }

  val userDao = bean("userDao") {
    new UserDao
  }

  val userService = bean("userService") {
    new UserService(userDao())
  }

  bean("notesController") {
    new NotesController(notesDao())
  }

  bean("applicationController") {
    new Application(userService())
  }

}