
import dao.UserDao
import injection.AppConfiguration
import models.User
import org.mindrot.jbcrypt.BCrypt
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import play.api._
import play.api.mvc.WithFilters
import filters.Filters
import scala.concurrent.ExecutionContext.Implicits.global


object Global extends WithFilters(Filters.gzipFilter) with GlobalSettings {

  val applicationContext = FunctionalConfigApplicationContext[AppConfiguration]

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    applicationContext.getBean(controllerClass)
  }

  override def onStart(app: Application) {
    val userDao: UserDao = applicationContext.getBean(classOf[UserDao])
    userDao.findByUsername("ben@ben.com").map {
      case None => userDao.insert(User("ben@ben.com", BCrypt.hashpw("ben", BCrypt.gensalt)))
      case Some(user) =>
    }
  }
}