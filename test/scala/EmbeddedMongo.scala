package scala

import org.specs2.specification.BeforeAfterExample
import de.flapdoodle.embed.mongo._
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.config.{IMongodConfig, Net, MongodConfigBuilder}
import de.flapdoodle.embed.process.runtime.Network

trait EmbeddedMongo extends BeforeAfterExample {

  def embedConnectionPort(): Int = {
    12345
  }

  def embedMongoDBVersion(): Version = {
    Version.V2_4_8
  }

  def inMemoryMongoDatabase(name: String = "default"): Map[String, String] = {
    Map("mongodb.uri" -> "mongodb://127.0.0.1:12345/wset-sat")
  }

  val mongoConfig: IMongodConfig = new MongodConfigBuilder().version(embedMongoDBVersion()).net(new Net(embedConnectionPort(), Network.localhostIsIPv6())).build()

  lazy val runtime: MongodStarter = MongodStarter.getDefaultInstance
  lazy val mongodExe: MongodExecutable = runtime.prepare(mongoConfig)

  lazy val mongod: MongodProcess = mongodExe.start()

  def before {
    mongod
  }

  def after {
    mongod.stop()
  }
}