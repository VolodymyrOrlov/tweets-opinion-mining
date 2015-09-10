package com.vorlov.classifier.naivebayes

import org.scalatest.{Matchers, WordSpec}

class NaiveBayesClassifierSpec extends WordSpec with Matchers {

  s"NaiveBayesClassifier" should {

    "correctly build a model" in {

      val classifier = new NaiveBayesClassifier

      val model = classifier.train{Seq(
        ("china", "Chinese Beijing Chinese"),
        ("china", "Chinese Chinese Shanghai"),
        ("china", "Chinese Macao"),
        ("japan", "a Tokyo Japan Chinese")
      )}

      model should === (NaiveBayesClassifierModel(Map(
        "china" -> NaiveBayesClassifierClass("china", Map("Chinese" -> 5, "Beijing" -> 1, "Shanghai" -> 1, "Macao" -> 1), 3),
        "japan" -> NaiveBayesClassifierClass("japan", Map("Tokyo" -> 1, "Japan" -> 1, "Chinese" -> 1), 1))))

      model.asInstanceOf[NaiveBayesClassifierModel].vocabularySize should === (6)

    }

    "correctly classify a document" in {

      val classifier = new NaiveBayesClassifier

      val model = classifier.train{Seq(
        ("china", "Chinese Beijing Chinese"),
        ("china", "Chinese Chinese Shanghai"),
        ("china", "Chinese Macao"),
        ("japan", "a Tokyo Japan Chinese")
      )}

      classifier.classify(model, "Chinese Chinese Chinese Tokyo Japan") should === ("china")

    }

  }

}
