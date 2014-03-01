package controllers

import play.api.mvc._
import models.{User, Login}
import models.Login.loginFormat
import models.User.userFormat
import scala.concurrent.Future
import play.api.libs.json.Json

import play.api.cache.Cache
import play.api.mvc.Cookie
import dao.UserDao
import scala.concurrent.ExecutionContext.Implicits.global

import java.util.UUID.randomUUID
import org.mindrot.jbcrypt.BCrypt

trait Security { self: Controller =>

  lazy implicit val app: play.api.Application = play.api.Play.current

  val AuthTokenHeader = "X-XSRF-TOKEN"
  val AuthTokenCookieKey = "XSRF-TOKEN"

  def HasToken[A](p: BodyParser[A] = parse.anyContent)(f: String => String => Request[A] => Result): Action[A] =
    Action(p) { implicit request =>
      val maybeToken = request.headers.get(AuthTokenHeader)
      maybeToken flatMap { token =>
        val tokenOption: Option[String] = Cache.getAs[String](token)

        tokenOption map { userid =>
          f(token)(userid)(request)
        }
      } getOrElse Unauthorized(Json.obj("err" -> "No Token"))
    }
}

class Application(userDao: UserDao) extends Controller with Security {

  lazy val CacheExpiration = app.configuration.getInt("cache.expiration").getOrElse(60 * 2)

  def loadPublicHTML(any: String) = controllers.Assets.at(path = "/public/html", s"$any.html")

  def index(any: String) = Action {
    Ok(views.html.index())
  }

  implicit class ResultWithToken(result: SimpleResult) {
     def withToken(token: (String, String)): SimpleResult = {
       Cache.set(token._1, token._2, CacheExpiration)
       result.withCookies(Cookie(AuthTokenCookieKey, token._1, None, httpOnly = false))
     }

     def discardingToken(token: String): SimpleResult = {
       Cache.remove(token)
       result.discardingCookies(DiscardingCookie(name = AuthTokenCookieKey))
     }
   }

  def login() = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Login].map {
        login =>
          userDao.findByUsername(login.username).map {
            user =>
              if(BCrypt.checkpw(login.password, user.get.password)) {
                Ok(Json.toJson(user.get)).withToken(randomUUID().toString -> user.get.username)
              } else {
                NotFound
              }
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def register() = Action.async(parse.json) {
    request =>
      request.body.validate[Login].map {
        login  =>
          val user: User = User(login.username, BCrypt.hashpw(login.password, BCrypt.gensalt))
          userDao.insert(user).map {
            lastError =>
              Created.withToken(randomUUID().toString -> user.username)
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def checkCredentials() = HasToken() { token => userId => implicit request =>
    Ok(Json.obj("ok"->"ok"))
  }

}