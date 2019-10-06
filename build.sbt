name := "scrala"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5.8",
  "org.jsoup" % "jsoup" % "1.12.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.json4s" %% "json4s-native" % "3.6.6"
)
