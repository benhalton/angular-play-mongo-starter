package scala.browser

import org.specs2.mutable.Specification
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

trait BrowserSpecification extends Specification {

  lazy val FIREFOX = classOf[FirefoxDriver]
  lazy val HTMLUNIT = classOf[HtmlUnitDriver]
  lazy val PHANTOM = classOf[PhantomJSDriver]


}