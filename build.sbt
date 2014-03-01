name := "wset-sat"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  filters,
  "org.reactivemongo" %% "reactivemongo" % "0.10.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.0",
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.41",
  "org.springframework.scala" % "spring-scala_2.10" % "1.0.0.RC1",
  "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0",
  "org.mindrot" % "jbcrypt" % "0.3m"
  )

resolvers ++= Seq(
  "spring scala milestones" at "http://repo.springsource.org/milestone"
  )

play.Project.playScalaSettings

lazy val jsTest = taskKey[Int]("jsTest")

jsTest in Test := {
  "karma start karma.conf.js" !
}

test := Def.taskDyn {
  val exitCode = (jsTest in Test).value
  if(exitCode == 0)
    Def.task {
      (test in Test).value
    }
  else Def.task()
}.value
