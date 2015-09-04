package com.vorlov.api.twitter.protocol

import com.vorlov.model.{TwitterSearchResponse, TwitterStatus}
import spray.json._

object TwitterProtocol extends DefaultJsonProtocol {

  implicit object TwitterStatusReader extends RootJsonReader[TwitterStatus] {

    def read(value: JsValue) = {

      value.asJsObject.getFields("text") match {

        case Seq(JsString(text)) =>

          TwitterStatus(text)

      }

    }
  }

  implicit object TwitterSearchResponseReader extends RootJsonReader[TwitterSearchResponse] {

    def read(value: JsValue) = {

      value.asJsObject.getFields("statuses") match {

        case Seq(JsArray(statuses)) =>

          TwitterSearchResponse(statuses.map(_.convertTo[TwitterStatus]))

      }

    }
  }

}
