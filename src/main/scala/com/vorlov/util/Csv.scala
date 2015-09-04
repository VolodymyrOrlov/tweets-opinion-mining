package com.vorlov.util

import org.apache.commons.lang3.StringEscapeUtils

object Csv {

  abstract class CSVFormat[T] {

    def asCSVHeader(data: T) = cells(data).map(v => s"""${v._1}""").mkString(",")

    def cells(data: T): Iterable[(String, Any)]

    def asCSV(data: T): String = cells(data).map(_._2) collect {
      case s: String => StringEscapeUtils.escapeCsv(s)
      case v => Option(v) map (_.toString) getOrElse("")
    } mkString(",")

  }


  implicit class IterableReportFormat[T](iterable: Iterator[T])(implicit format: CSVFormat[T]) extends Iterator[String] {

    private lazy val firstLine = if(iterable.hasNext) Option(iterable.next()) else None

    private lazy val header = firstLine.map{
      line =>
        Iterator.single{
          format.asCSVHeader(line)
        } ++ Iterator.single{
          format.asCSV(line)
        }
    } getOrElse(Iterator.empty)

    override def hasNext: Boolean = header.hasNext || iterable.hasNext

    override def next(): String = header.hasNext match {
      case true => header.next
      case false => format.asCSV(iterable.next)

    }

    def asCSV = this

  }


}
