package com.vorlov.util

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormat}
import org.joda.time.{DateTimeZone, Period, DateTime}

object Date {

  implicit class DateRichDate(date: DateTime) {

    def dateRange(to: DateTime, step: Period): Iterator[DateTime] = Iterator.iterate(date)(_.plus(step)).takeWhile(!_.isAfter(to))

    def yesterday = if(date.hourOfDay() == 0 && date.minuteOfDay() == 0) date else date.minusDays(1)

    def iso8601Str = date.withZone(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTimeNoMillis())

  }

  implicit class DateRichString(str: String) {

    val formatters = Seq(
      DateTimeFormat.forPattern("MMM dd HH:mm:ss Z yyyy"),
      DateTimeFormat.forPattern("E MMM dd HH:mm:ss Z yyyy"),
      ISODateTimeFormat.dateTimeNoMillis
    )

    val formatter = new DateTimeFormatterBuilder().append(null, formatters.map(_.getParser).toArray).toFormatter;

    def toDateTime = {
      DateTime.parse(str, formatter)
    }

  }

}
