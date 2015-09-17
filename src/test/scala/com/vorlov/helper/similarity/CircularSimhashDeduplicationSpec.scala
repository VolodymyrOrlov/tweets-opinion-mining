package com.vorlov.helper.similarity

import org.scalatest.{Matchers, WordSpec}
import com.vorlov.util.TwitterTokenizer._

class CircularSimhashDeduplicationSpec extends WordSpec with Matchers {

  implicit def stringify(str: String): String = str.tokens.mkString

  "remove similar documents when all documents are similar" in {

    val dataset = Seq(
      "As collected deficient objection by it discovery sincerity curiosity",
      "As collected deficient objection by it discovery sincerity curiosity",
      "As collected deficient objection by it discovery sincerity curiosity",
      "As collected deficient objection by it discovery sincerity curiosity"
    )

    CircularSimhashDeduplication.deduplicate(dataset)(stringify).size should === (dataset.distinct.size)

  }

  "CircularSimhashDeduplication" should {

    "remove similar documents" in {

      val dataset = Seq(
        "As collected deficient objection by it discovery sincerity curiosity", //1
        "As collected deficient objection by it discovery sincerity trust", //1
        "As collected deficient objection by it discovery sincerity curiosity", //1
        "Quiet decay who round three world whole has mrs man", //2
        "Assure in adieus wicket it is", //3
        "Offending her moonlight men sweetness see unwilling", //4
        "Often of it tears whole oh balls share an", //5
        "Quiet decay who round three world whole has mrs", //2
        "As collected deficient objection by it discovery sincerity curiosity", //1
        "Very often of it tears whole oh balls share an" //5
      )

      CircularSimhashDeduplication.deduplicate(dataset, 0.2)(stringify).size should === (5)

    }

    "do not fail when there are no documents" in {

      CircularSimhashDeduplication.deduplicate(Seq.empty[String])(stringify).size should === (0)

    }

  }


}
