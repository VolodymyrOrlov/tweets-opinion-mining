package com.vorlov.helper.similarity

import org.slf4j.LoggerFactory

import scala.annotation.tailrec

object CircularSimhashDeduplication {

  implicit val log = LoggerFactory.getLogger(getClass.getName)

  import com.vorlov.util.StringUtils._
  import com.vorlov.util.Log._

  def deduplicate[T](data: Seq[T])(implicit stringify: T => String): Seq[T] = deduplicate(data, 0.2, 32, 5)

  def deduplicate[T](data: Seq[T], similarityThreshold: Double)(implicit stringify: T => String): Seq[T] = deduplicate(data, similarityThreshold, 32, 5)

  @tailrec
  private def deduplicate[T](data: Seq[T], similarityThreshold: Double, windowSize: Int, shift: Int)(implicit stringify: T => String): Seq[T] = {
    def dedup = data.map(t => (Integer.rotateLeft(stringify(t).simhash, shift), t)).sortBy(_._1).foldLeft(Seq.empty[(Int, T)]) {
      (result, e) =>
        result.take(windowSize).exists(v => hashDistance(v._1, e._1) < 2 && distance(v._2, e._2) < similarityThreshold) match {
          case true => result
          case false => e +: result
        }
    }.map(_._2)

    shift match {
      case n if n < 1 => dedup
      case n => deduplicate(dedup, similarityThreshold, windowSize, shift - 1)
    }
  }

  def hashDistance(hash1: Int, hash2: Int) = Integer.toBinaryString(hash1).levenshteinDistance(Integer.toBinaryString(hash2))

  def distance(a: String, b: String): Double = debug({
    a.levenshteinDistance(b) / math.max(b.size, a.size).toDouble
  }, s"distance($a, $b)")



}
