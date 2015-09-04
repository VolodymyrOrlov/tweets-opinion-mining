package com.vorlov.api.twitter

import akka.actor.ActorSystem
import com.vorlov.api.twitter.model.{TwitterSearchResponse, Tweet}
import org.slf4j.LoggerFactory
import spray.http.{HttpRequest, HttpResponse}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

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

  def search(query: String, lang: String = "en", count: Int = 100, maxID: Option[Long] = None): Future[Seq[Tweet]] = {

    val pipeline: HttpRequest => Future[TwitterSearchResponse] = oauthSignRequest ~> logRequest ~> sendReceive ~> logResponse ~> unmarshal[TwitterSearchResponse]

    pipeline(Get(s"$base?q=$query&lang=$lang&count=$count" + maxID.map(id => s"&max_id=$id").getOrElse(""))).map(_.statuses)

  }

  def traverse(query: String, lang: String = "en", timeout: Duration = 10 seconds): Stream[Tweet] = {

    def recursive(maxID: Option[Long]): Stream[Tweet] = {

      Await result(search(query, lang = lang, maxID = maxID), timeout) match {
        case tweets if !tweets.isEmpty => tweets.toStream.append(recursive(Some(tweets.last.id - 1)))
        case _ => Stream.empty
      }

    }

    recursive(None)

  }


}
