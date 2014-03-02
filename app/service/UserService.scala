package service

import org.mindrot.jbcrypt.BCrypt
import dao.UserDao
import scala.concurrent.Future
import models.User
import reactivemongo.core.commands.LastError


class UserService(userDao: UserDao) {

  def checkPassword(plainText: String, hashed: String): Boolean = {
    BCrypt.checkpw(plainText, hashed)
  }

  def findByUsername(username: String): Future[Option[User]] = {
    userDao.findByUsername(username)
  }

  def insert(user: User): Future[LastError] = {
    userDao.insert(user)
  }


}
