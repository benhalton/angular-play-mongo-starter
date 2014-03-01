package models

import play.api.libs.json.Json


case class Login(username: String, password: String) {

}

object Login {

  implicit val loginFormat = Json.format[Login]

}