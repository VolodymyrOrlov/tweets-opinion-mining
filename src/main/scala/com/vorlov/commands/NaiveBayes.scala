package com.vorlov.commands

import java.io.File

import com.vorlov.util
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.slf4j.LoggerFactory

import com.vorlov.helper._
import util.Csv._
import util.TwitterTokenizer._

object NaiveBayes extends Command {

  val log = LoggerFactory.getLogger(getClass.getName)

  val inputPath = configuration.getString("tweets-opinion-mining.data.path")

  log.info(s"Reading data from $inputPath")

  val tokenizer = new StandardAnalyzer()

  val tweets = new File(inputPath).asCSV

  val bagOfWords = tweets.flatMap(_.text.tokens).toSet

  println(bagOfWords.size)

  println(bagOfWords)

}
