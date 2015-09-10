package com.vorlov.classifier

import scala.annotation.tailrec

abstract class Classifier {

  type ClassifierModel

  type LabeledText = (String, String)

  final def train(samples: Iterable[LabeledText]): ClassifierModel = train(samples, None)

  @tailrec
  final def train(samples: Iterable[(String, String)], model: Option[ClassifierModel]): ClassifierModel = (samples, model) match {
    case (h :: t, m) => train(t, Some(train(h._1, h._2, m)))
    case (Nil, Some(m)) => m
    case (Nil, _) => throw new IllegalArgumentException("Can not create a model out of empty sample set.")
  }

  protected def train(category: String, sample: String, model: Option[ClassifierModel]): ClassifierModel

  def classify(model: ClassifierModel, text: String): String

}
