package com.vorlov.commands

import java.io.File

import com.vorlov.classifier.naivebayes.NaiveBayesClassifier
import com.vorlov.util
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.slf4j.LoggerFactory

import com.vorlov.helper._
import util.Csv._

object NaiveBayes extends Command {

  val log = LoggerFactory.getLogger(getClass.getName)

  val classifier = new NaiveBayesClassifier

  val inputPath = configuration.getString("tweets-opinion-mining.data.path")

  log.info(s"Reading data from $inputPath")

  val tokenizer = new StandardAnalyzer()

  val tweets = new File(inputPath).asCSV

}
