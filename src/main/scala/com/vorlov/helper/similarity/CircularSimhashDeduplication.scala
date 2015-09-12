package com.vorlov.helper.similarity

import org.slf4j.LoggerFactory

object CircularSimhashDeduplication {

  implicit val log = LoggerFactory.getLogger(getClass.getName)

  import com.vorlov.util.StringUtils._
  import com.vorlov.util.Log._

  def deduplicate[T](tweets: Stream[T])(implicit stringify: T => String): Stream[T] = deduplicate(tweets, 31)

  private def deduplicate[T](tweets: Stream[T], shift: Int)(implicit stringify: T => String): Stream[T] = {
    def dedup = tweets.map(t => (Integer.rotateLeft(stringify(t).simhash, shift), t)).sortBy(_._1).sliding(2).filter{
      _ match {
        case Stream((hash1, tweet1), (hash2, tweet2)) if hashDistance(hash1, hash2) < 2 && distance(tweet1, tweet2) < 0.3 => false
        case _ => true
      }
    }.map(_.head._2).toStream

    shift match {
      case n if n < 1 => dedup
      case n => deduplicate(dedup, shift - 1)
    }
  }

  def hashDistance(hash1: Int, hash2: Int): Int = Integer.toBinaryString(hash1).levenshteinDistance(Integer.toBinaryString(hash2))

  def distance(a: String, b: String): Double = debug({
    a.levenshteinDistance(b) / ((a.size + b.size) / 2.0)
  }, s"distance($a, $b)")



}
