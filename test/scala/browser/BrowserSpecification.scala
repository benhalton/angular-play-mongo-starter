package scala.browser

import org.specs2.mutable.Specification
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.phantomjs.{PhantomJSDriverService, PhantomJSDriver}
import java.nio.file.{FileSystems, Files}

trait BrowserSpecification extends Specification {

  lazy val FIREFOX = classOf[FirefoxDriver]
  lazy val HTMLUNIT = classOf[HtmlUnitDriver]
  lazy val PHANTOM = {

    val PhantomRelativePath = "./node_modules/.bin/phantomjs"

    if (Files.exists(FileSystems.getDefault.getPath(PhantomRelativePath))) {
      System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PhantomRelativePath)
    }

    classOf[PhantomJSDriver]
  }

}