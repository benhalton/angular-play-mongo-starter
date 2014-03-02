package scala.unit

import org.mockito.Mockito._
import dao.UserDao
import play.api.test.{PlaySpecification, FakeRequest}
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.mvc.{Results, SimpleResult}
import scala.concurrent.ExecutionContext.Implicits.global
import org.specs2.mock._


class ApplicationControllerSpec extends PlaySpecification with Mockito with Results {

  val userDao: UserDao = mock[UserDao]
  val appController = new controllers.Application(userDao)

  "login" should {
    "return NotFound when the username does not match an existing user" in {

      when(userDao.findByUsername(anyString)).thenReturn(Future(None))

      val request = FakeRequest().withBody(Json.obj("username"->"ben@ben.com", "password"->"password"))
      val login: Future[SimpleResult] = appController.login()(request)
      status(login) must equalTo(NotFound.header.status)

    }
  }
  

}
- 
