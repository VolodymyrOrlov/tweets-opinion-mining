package com.vorlov.helper.similarity

import org.scalatest.{Matchers, WordSpec}
import com.vorlov.util.TwitterTokenizer._

class CircularSimhashDeduplicationSpec extends WordSpec with Matchers {

  implicit def stringify(str: String): String = str.tokens.mkString

  "remove similar documents when all documents are similar" in {

    val dataset = Seq(
      "one",
      "one",
      "one",
      "one"
    )

    CircularSimhashDeduplication.deduplicate(dataset)(stringify).size should === (dataset.distinct.size)

  }

  "CircularSimhashDeduplication" should {

    "remove similar documents" in {

      val dataset = Seq(
        "one",
        "two",
        "three",
        "six",
        "four",
        "five",
        "six",
        "six",
        "seven",
        "two",
        "two",
        "eight",
        "nine",
        "ten"
      )

      CircularSimhashDeduplication.deduplicate(dataset)(stringify).size should === (10)

    }

    "do not fail when there are no documents" in {

      CircularSimhashDeduplication.deduplicate(Seq.empty[String])(stringify).size should === (0)

    }

  }


}
