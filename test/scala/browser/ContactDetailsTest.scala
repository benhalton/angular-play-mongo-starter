package scala.browser

import org.openqa.selenium.phantomjs.PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import play.api.test.{TestBrowser, FakeApplication, WithBrowser}
import org.openqa.selenium.Dimension
import org.fest.assertions.Assertions._

class ContactDetailsTest extends BrowserSpecification with EmbeddedMongo {

  class WithWSETSAT extends WithBrowser(PHANTOM, FakeApplication(additionalConfiguration = inMemoryMongoDatabase())) {}

  class WithPhantomJs extends WithWSETSAT {
    lazy override val browser: TestBrowser = {
      val defaultCapabilities = DesiredCapabilities.phantomjs
      val additionalCapabilities = new DesiredCapabilities()
      additionalCapabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "node_modules/phantomjs/bin/phantomjs")
      val capabilities = new DesiredCapabilities(defaultCapabilities, additionalCapabilities)
      val driver = new PhantomJSDriver(capabilities)
      TestBrowser(driver, Some("http://localhost:" + port))
    }
  }


  "run in a browser" in new WithPhantomJs {
    browser.manage.window.setSize(new Dimension(1080, 600))
    browser.goTo("/")
    assertThat(browser.title()).isEqualTo("WSET Systematic Approach to Tasting")
    browser.$("#contact-tab").click()
    assertThat(browser.$("#ben").getText).isEqualTo("ben@winosoft.co.uk")
  }

}