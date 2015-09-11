package com.vorlov.util

object StringUtils {

  implicit class StringUtilsRishString(str: String) {

    def simhash() = {

      val v = new Array[Int](32)

      for (feature <- shingles.map(_.hashCode)) {
        var n = feature
        for (b <- 0 until 32) {
          v(b) += (if ((n & 1) == 1) 1 else -1)
          n = n >>> 1
        }
      }

      var sim = 0
      for (b <- v.map(_ > 0))
        sim = sim << 1 | (if (b) 1 else 0)

      sim

    }

    def shingles() = str.sliding(2)

    def shingles(size: Int) = str.sliding(size)

    def levenshteinDistance[A](that: Iterable[A]) =
      ((0 to that.size).toList /: str.toCharArray)((prev, x) =>
        (prev zip prev.tail zip that).scanLeft(prev.head + 1) {
          case (h, ((d, v), y)) => math.min(math.min(h + 1, v + 1), d + (if (x == y) 0 else 1))
        }) last

  }


}
