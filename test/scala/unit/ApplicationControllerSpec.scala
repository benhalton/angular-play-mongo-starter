package scala.unit

import org.mockito.Mockito._
import play.api.test.{PlaySpecification, FakeRequest}
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.mvc.{Results, SimpleResult}
import scala.concurrent.ExecutionContext.Implicits.global
import org.specs2.mock._
import service.UserService
import models.User


class ApplicationControllerSpec extends PlaySpecification with Mockito with Results {

  val userService: UserService = mock[UserService]
  val appController = new controllers.Application(userService)

  private val username = "ben@ben.com"

  "login" should {

    "return NotFound when the username does not match an existing user" in {

      when(userService.findByUsername(username)).thenReturn(Future(None))

      val request = FakeRequest().withBody(Json.obj("username" -> username, "password" -> "password"))
      val login: Future[SimpleResult] = appController.login()(request)
      status(login) must equalTo(NotFound.header.status)

    }

    "return NotFound if the username matches but the password does not" in {
      when(userService.findByUsername(username)).thenReturn(Future(Some(User("ben", "hashed"))))
      when(userService.checkPassword("password", "hashed")).thenReturn(false)
      val request = FakeRequest().withBody(Json.obj("username" -> username, "password" -> "password"))
      val login: Future[SimpleResult] = appController.login()(request)
      status(login) must equalTo(NotFound.header.status)
    }

  }


}
