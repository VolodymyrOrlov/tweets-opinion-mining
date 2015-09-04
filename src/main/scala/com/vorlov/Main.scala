package com.vorlov

import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.vorlov.api.twitter.{Token, TwitterAPI}
import com.vorlov.util.IOUtils

import scala.concurrent.Await

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  def help() {
    println(s"\nUsage: ${this.getClass.getName} <configuration file>\n")
  }

  if (args.length < 1) {
    Console.err.println("Incorrect number of input arguments.")
    help()
    sys.exit(1)
  }

  if (!IOUtils.isReadable(args(0))) {
    Console.err.println("Could not open configuration file.")
    sys.exit(2)
  }

  implicit val config = ConfigFactory.parseFile(new File(args(0)))

  implicit val system = ActorSystem("TweetsOpinionMining", config)

  val twitterApi = new TwitterAPI(
    Token(config.getString("tweets-opinion-mining.token.consumer.key"), config.getString("tweets-opinion-mining.token.consumer.secret")),
    Token(config.getString("tweets-opinion-mining.token.access.key"), config.getString("tweets-opinion-mining.token.access.secret")))

  for { status <- Await result(twitterApi.search("sungevity"), 10 seconds) } yield println(status)

  system.shutdown()

}
