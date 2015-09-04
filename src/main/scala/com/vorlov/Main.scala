package com.vorlov

import java.io.File
import java.nio.file.StandardOpenOption

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.vorlov.api.twitter.model.Tweet
import com.vorlov.api.twitter.{Token, TwitterAPI}
import com.vorlov.util.IOUtils

import util.Csv._


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

  val outputPath = config.getString("tweets-opinion-mining.output.path")

  implicit val TweetCsvFormat = new CSVFormat[Tweet] {
    override def cells(tweet: Tweet): Iterable[(String, Any)] = Seq(
      ("id" -> tweet.id),
      ("text" -> tweet.text),
      ("language" -> tweet.language),
      ("user" -> tweet.user.name)
    )
  }

  twitterApi.stream(config.getString("tweets-opinion-mining.input.query")).toIterator.asCSV.foreach{

    line =>

      IOUtils.write(outputPath, line + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE)
  }

  system.shutdown()

}
