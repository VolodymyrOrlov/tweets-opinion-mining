package com.vorlov.util

import org.scalatest.{Matchers, WordSpec}

import StringUtils._

class StringUtilsSpec  extends WordSpec with Matchers {

  "StringUtils" should {

    "correctly calculate shingles of a string" in {
      "the cat sat on the mat".shingles.toList should === (List("th", "he", "e ", " c", "ca", "at", "t ", " s", "sa", "at", "t ", " o", "on", "n ", " t", "th", "he", "e ", " m", "ma", "at"))

    }

    "correctly calculate simhash of a string" in {

      println(Integer.rotateLeft("the cat sat on the mat".simhash, 1))

      "the cat sat on the mat".simhash should === (-97517568)

      "the cat sat on a mat".simhash should === (-97517568)

      "we all scream for ice cream".simhash should === (-768606208)
    }

    "correctly calculate levenshtein distance between 2 strings" in {

      "The quick brown fox".levenshteinDistance("The quick brown fox") should === (0)

      "The quick brown fox".levenshteinDistance("xof nworb kciuq ehT") should === (16)

    }

  }

}
