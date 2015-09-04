package com.vorlov.util

import spray.json._

import scala.collection.immutable

object JSONUtils {

  implicit class RichJsValue(value: JsValue) {

    def asOptString: Option[String] = value match {
      case JsString(value) => Some(value)
      case _ => None
    }

    def asOptBoolean: Option[Boolean] = value match {
      case JsBoolean(value) => Some(value)
      case _ => None
    }

    def asOptObject: Option[JsObject] = value match {
      case obj: JsObject => Some(obj)
      case _ => None
    }

    def asOptInt: Option[Int] = asOptNumber(_.toInt)

    def asOptLong: Option[Long] = asOptNumber(_.toLong)

    def asOptDouble: Option[Double] = asOptNumber(_.toDouble)

    private def asOptNumber[T](fConvert: BigDecimal => T): Option[T] = value match {
      case JsNumber(value) => Some(fConvert(value))
      case _ => None
    }

  }

  implicit class JSONUtilsRichJsObject(obj: JsObject) {

    def safeGetFields(fieldNames: String*): immutable.Seq[JsValue] = fieldNames.map(obj.fields.getOrElse(_, JsNull))(collection.breakOut)

  }

}
