package scala.browser

import play.api.test.{FakeApplication, WithBrowser}
import org.openqa.selenium.Dimension
import org.fest.assertions.Assertions._
class ContactDetailsTest extends BrowserSpecification with EmbeddedMongo {

  class WithWSETSAT extends WithBrowser(PHANTOM, FakeApplication(additionalConfiguration = inMemoryMongoDatabase())) {}


  "run in a browser" in new WithWSETSAT {

    browser.manage.window.setSize(new Dimension(1080, 600))
    browser.goTo("/")
    assertThat(browser.title()).isEqualTo("WSET Systematic Approach to Tasting")
    browser.$("#contact-tab").click()
    assertThat(browser.$("#ben").getText).isEqualTo("ben@winosoft.co.uk")
  }

}