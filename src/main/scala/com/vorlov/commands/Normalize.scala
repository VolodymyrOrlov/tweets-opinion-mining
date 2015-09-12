package com.vorlov.commands

import java.io.File
import java.nio.file.StandardOpenOption

import com.vorlov.api.twitter.model.Tweet
import com.vorlov.classifier.naivebayes.NaiveBayesClassifier
import com.vorlov.helper.similarity.CircularSimhashDeduplication
import com.vorlov.util.IOUtils
import org.slf4j.LoggerFactory
import com.vorlov.helper.format.csv._

import com.vorlov.util.Csv._
import com.vorlov.util.TwitterTokenizer._

object Normalize extends Command {

  implicit val log = LoggerFactory.getLogger(getClass.getName)
  
  def tweetIDDeduplicate(tweets: Stream[Tweet]): Stream[Tweet] = tweets.sortBy(_.id).sliding(2).filter{
    _ match {
      case Stream(t1, t2) if t1.id == t2.id => false
      case _ => true
    }
  }.map(_.head).toStream

  val classifier = new NaiveBayesClassifier

  val inputPath = configuration.getString("tweets-opinion-mining.data.raw.path")
  val outputPath = configuration.getString("tweets-opinion-mining.data.normalized.path")

  log.info(s"Reading data from $inputPath")

  val tweets = new File(inputPath).asCSV.toStream

  if(IOUtils.exists(outputPath)) IOUtils.delete(outputPath)

  log.info(s"Writing data to $outputPath")

  implicit def tweetToString(tweet: Tweet): String = tweet.text.tokens.mkString

  CircularSimhashDeduplication.deduplicate(tweetIDDeduplicate(tweets)).toIterator.asCSV.foreach {
    line =>
      IOUtils.write(outputPath, line + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE)
  }

}
