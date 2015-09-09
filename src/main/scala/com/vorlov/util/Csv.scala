package com.vorlov.util

import java.io.File

import org.apache.commons.lang3.StringEscapeUtils

import scala.annotation.tailrec
import scala.io.Source

object Csv {

  abstract class CSVFormat[T] {

    def asCSVHeader(data: T) = write(data).map(v => s"""${v._1}""").mkString(",")

    def write(data: T): Iterable[(String, Any)]

    def read(cells: Seq[String]): T

    def asCSV(data: T): String = write(data).map(_._2) collect {
      case s: String => StringEscapeUtils.escapeCsv(s)
      case v => Option(v) map (_.toString) getOrElse("")
    } mkString(",")

    def fromCSV(line: String): T = read(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)").map(StringEscapeUtils.unescapeCsv))

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

  implicit class CsvRichFile[T](file: File)(implicit format: CSVFormat[T]) extends Iterator[T] {

    private val lines = Source.fromFile(file).getLines()

    @tailrec
    private def nextLine(prev: String = ""): String = prev + lines.next() match {
      case line if (line.count(_ == '"')) % 2 == 0 => line
      case line => nextLine(line)
    }

    val skippedHeader = nextLine()

    override def hasNext: Boolean = lines.hasNext

    override def next(): T = format.fromCSV(nextLine())

    def asCSV = this
  }


}
