package com.vorlov.commands

import java.nio.file.StandardOpenOption

import akka.actor.ActorSystem
import com.vorlov.api.twitter.{Token, TwitterAPI}
import com.vorlov.util

import com.vorlov.util.IOUtils
import org.slf4j.LoggerFactory

import com.vorlov.helper._
import util.Csv._

import scala.concurrent.ExecutionContext.Implicits.global

object FetchData extends Command {

  val log = LoggerFactory.getLogger(getClass.getName)

  implicit val system = ActorSystem("TweetsOpinionMining", configuration)

  val twitterApi = new TwitterAPI(
    Token(configuration.getString("tweets-opinion-mining.twitter.token.consumer.key"), configuration.getString("tweets-opinion-mining.twitter.token.consumer.secret")),
    Token(configuration.getString("tweets-opinion-mining.twitter.token.access.key"), configuration.getString("tweets-opinion-mining.twitter.token.access.secret")))

  val outputPath = configuration.getString("tweets-opinion-mining.data.path")

  log.info(s"Loading data into $outputPath")

  twitterApi.stream(configuration.getString("tweets-opinion-mining.twitter.query")).toIterator.asCSV.foreach{

    line =>

      IOUtils.write(outputPath, line + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE)
  }

  system.shutdown()

}
