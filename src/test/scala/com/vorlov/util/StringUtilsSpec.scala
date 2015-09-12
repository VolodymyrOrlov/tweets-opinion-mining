package com.vorlov.util

import org.scalatest.{Matchers, WordSpec}

import StringUtils._
import TwitterTokenizer._

class StringUtilsSpec  extends WordSpec with Matchers {

  "StringUtils" should {

    "correctly calculate shingles of a string" in {
      "the cat sat on the mat".shingles.toList should === (List("th", "he", "e ", " c", "ca", "at", "t ", " s", "sa", "at", "t ", " o", "on", "n ", " t", "th", "he", "e ", " m", "ma", "at"))

    }

    "correctly calculate simhash of a string" in {

      println(Integer.toBinaryString("RT @LAKings: .@KingsVision caught up w/ Bill Ranford, Patrik Bartosak, &amp; Adrian Kempe at Day 1 of Rookie Camp pres by @Sungevity!https://tâ€¦".tokens.mkString(" ").simhash()))

      println(Integer.toBinaryString("RT @LAKings: .@KingsVision caught up w/ Bill Ranford, Patrik Bartosak, &amp; Adrian Kempe at Day 1 of Rookie Camp pres by @Sungevity!https://tâ€¦".tokens.mkString(" ").simhash()))

      "the cat sat on the mat".simhash should === (-1370850857)

      "the cat sat on a mat".simhash should === (-1370850857)

      "we all scream for ice cream".simhash should === (-1437959721)

    }

    "correctly calculate levenshtein distance between 2 strings" in {

      "The quick brown fox".levenshteinDistance("The quick brown fox") should === (0)

      "The quick brown fox".levenshteinDistance("xof nworb kciuq ehT") should === (16)

    }

  }

}
