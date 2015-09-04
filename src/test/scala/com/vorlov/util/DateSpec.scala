package com.vorlov.util

import org.scalatest.{Matchers, WordSpec}

import Date._

class DateSpec extends WordSpec with Matchers {

  "Date utilities" should {

    "correctly parse date" in {

      "Sep 04 18:22:30 +0000 2015".toDateTime.toISO8601Str should === ("2015-09-04T18:22:30Z")
      "Fri Sep 04 18:22:30 +0000 2015".toDateTime.toISO8601Str should === ("2015-09-04T18:22:30Z")

    }

  }

}
