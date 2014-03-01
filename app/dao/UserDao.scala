package dao

import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.Future
import models.User
import play.api.libs.json.Json
import reactivemongo.core.commands.LastError
import reactivemongo.api.indexes.{IndexType, Index}


class UserDao extends Dao {

  lazy val collection = initCollection

  def initCollection = {
    val lazyCollection = db[JSONCollection]("users")
    lazyCollection.indexesManager.ensure(Index(Seq(("username", IndexType.Descending)), unique=true, dropDups=true))
    lazyCollection
  }


  def findByUsername(username: String): Future[Option[User]] = {
    collection.find(Json.obj("username" -> username)).one[User]
  }

  def insert(user: User): Future[LastError] = {
    collection.insert(user)
  }

}