package com.vorlov.classifier.naivebayes

import com.vorlov.classifier.Classifier
import com.vorlov.util
import org.slf4j.LoggerFactory

import util.TwitterTokenizer._
import util.Log._

case class NaiveBayesClassifierClass(name: String, terms: Map[String, Int], documentsCount: Int) {

  def +(document: Seq[String]): NaiveBayesClassifierClass = {

    NaiveBayesClassifierClass(name, document.foldLeft(terms) {
      (terms, term) =>
        terms.get(term) match {
          case None => terms + ((term, 1))
          case Some(count) => terms + ((term, count + 1 ))
        }
    }.withDefaultValue(0), documentsCount + 1)

  }

  lazy val termsCount = terms.values.sum

  override def toString() = name

}

object NaiveBayesClassifierClass {

  def empty(name: String) = NaiveBayesClassifierClass(name, Map.empty, 0)

}

case class NaiveBayesClassifierModel(classes: Map[String, NaiveBayesClassifierClass]) {

  lazy val vocabularySize = classes.foldLeft(Set.empty[String])((voc, cl) => voc ++ cl._2.terms.keySet).size

  lazy val documentsCount = classes.values.foldLeft(0)((count, cl) => count + cl.documentsCount)

}

class NaiveBayesClassifier extends Classifier {

  implicit val log = LoggerFactory.getLogger(getClass.getName)

  override type ClassifierModel = NaiveBayesClassifierModel

  override def train(category: String, sampleText: String, model: Option[ClassifierModel]): ClassifierModel = {

    NaiveBayesClassifierModel {
      model match {
        case None => Map(category -> (NaiveBayesClassifierClass.empty(category) + sampleText.tokens)).withDefault(name => NaiveBayesClassifierClass.empty(name))
        case Some(NaiveBayesClassifierModel(classes)) => classes + ((category, classes(category) + sampleText.tokens))
      }
    }

  }


  override def classify(model: ClassifierModel, text: String): String = {

    def termCount(term: String, category: NaiveBayesClassifierClass) = debug(category.terms(term), s"count($term in $category)")

    def categoryFrequency(category: NaiveBayesClassifierClass) = debug(category.documentsCount.toDouble / model.documentsCount.toDouble, s"P($category)")

    def termWeight(term: String, category: NaiveBayesClassifierClass) = debug((termCount(term, category) + 1.0) / (category.termsCount.toDouble + model.vocabularySize.toDouble), s"P($term|$category)")

    def weight(category: NaiveBayesClassifierClass) = categoryFrequency(category) * text.tokens.map(termWeight(_, category)).product

    model.classes.maxBy {
      category =>
        debug(weight(category._2), s"weight(${category._1})")
    }._1

  }
}
