package com.vorlov.classifier.naivebayes

import com.vorlov.classifier.Classifier

case class NaiveBayesClassifierModel()

class NaiveBayesClassifier extends Classifier {

  override type ClassifierModel = NaiveBayesClassifierModel

  override def train(category: String, sample: String, model: Option[ClassifierModel]): ClassifierModel = NaiveBayesClassifierModel()

  override def classify(model: ClassifierModel, text: String): String = ""
}
