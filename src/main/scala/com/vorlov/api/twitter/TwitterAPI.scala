package com.vorlov.api.twitter

import akka.actor.ActorSystem
import com.vorlov.api.twitter.model.{TwitterSearchResponse, TwitterStatus}
import org.slf4j.LoggerFactory
import spray.http.{HttpRequest, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

import spray.client.pipelining._
import spray.httpx.SprayJsonSupport._
import protocol.TwitterProtocol._

class TwitterAPI(override protected val consumer: Token, override protected val access: Token)(implicit system: ActorSystem, ec: ExecutionContext) extends OAuth1 {

  val log = LoggerFactory.getLogger(getClass.getName)

  val base = "https://api.twitter.com/1.1/search/tweets.json"

  val logRequest: HttpRequest => HttpRequest = { r => log.debug(r.entity.data.asString); r }

  val logResponse: HttpResponse => HttpResponse = {
    r =>

      r.status.intValue match {
        case 200 => log.debug(s"HTTP response: [${r.status.intValue}], [${r.entity.data.asString}]")
        case _ => log.warn(s"HTTP response: [${r.status.intValue}], [${r.entity.data.asString}]")
      }

      r
  }

  def search(query: String, count: Int = 100, lang: String = "en"): Future[Seq[TwitterStatus]] = {

    val pipeline: HttpRequest => Future[TwitterSearchResponse] = oauthSignRequest ~> logRequest ~> sendReceive ~> logResponse ~> unmarshal[TwitterSearchResponse]

    pipeline(Get(s"$base?q=$query&lang=$lang&count=$count")).map(_.statuses)

  }



}
