package com.vorlov.api.twitter

import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import akka.actor.ActorSystem
import com.vorlov.model.{TwitterSearchResponse, TwitterStatus}
import org.slf4j.LoggerFactory
import spray.http.{HttpRequest, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

import spray.client.pipelining._
import spray.httpx.SprayJsonSupport._
import protocol.TwitterProtocol._

class TwitterAPI(consumer: Token, access: Token)(implicit system: ActorSystem, ec: ExecutionContext) {

  val log = LoggerFactory.getLogger(getClass.getName)

  val base = "https://api.twitter.com/1.1/search/tweets.json"

  type HttpParameter = (String, String)

  val logRequest: HttpRequest => HttpRequest = { r => log.debug(r.entity.data.asString); r }

  val logResponse: HttpResponse => HttpResponse = {
    r =>

      r.status.intValue match {
        case 200 => log.debug(s"HTTP response: [${r.status.intValue}], [${r.entity.data.asString}]")
        case _ => log.warn(s"HTTP response: [${r.status.intValue}], [${r.entity.data.asString}]")
      }

      r
  }

  def oauthSignRequest: HttpRequest => HttpRequest = {
    case request =>
      addHeader("Authorization", authorizationHeader(request.method.name.toUpperCase, request.uri.toString))(request)
  }

  def search(query: String, count: Int = 100, lang: String = "en"): Future[Seq[TwitterStatus]] = {

    val pipeline: HttpRequest => Future[TwitterSearchResponse] = oauthSignRequest ~> logRequest ~> sendReceive ~> logResponse ~> unmarshal[TwitterSearchResponse]

    pipeline(Get(s"$base?q=$query&lang=$lang&count=$count")).map(_.statuses)

  }

  private def authorizationHeader (method: String, url: String): String = {
    val timestamp: Long = System.currentTimeMillis / 1000
    val nonce: Long = timestamp + Random.nextInt
    authorizationHeader(method, url, nonce.toString, timestamp.toString)
  }

  private def authorizationHeader (method: String, url: String, nonce: String, timestamp: String): String = {

    def sign(data: String) = {

      val mac: Mac = Mac.getInstance("HmacSHA1")

      val oauthSignature: String = consumer.secret + "&" + access.secret

      val spec = new SecretKeySpec(oauthSignature.getBytes, "HmacSHA1")

      mac.init(spec)

      val byteHMAC = mac.doFinal(data.getBytes)

      URLEncoder.encode(java.util.Base64.getEncoder.encodeToString(byteHMAC), "UTF-8")
    }

    val oauthHeaderParams = List[HttpParameter](
      ("oauth_consumer_key", consumer.key),
      ("oauth_signature_method", "HMAC-SHA1"),
      ("oauth_timestamp", timestamp),
      ("oauth_nonce", nonce),
      ("oauth_version", "1.0"),
      ("oauth_token", access.key)
    )

    val signatureBaseParams = (oauthHeaderParams ++ url.dropWhile(_ != '?').drop(1).split("&").map(_.split("=")).map{
      case Array(key, value) => (key, value)
      case Array(key) => (key, "")
    }).sorted

    val params = signatureBaseParams.map(p => p._1 + "=" + p._2).mkString("&")

    val components = List(method, url.takeWhile(_ != '?')) :+ params

    val oauthBaseString = components.map(URLEncoder.encode(_, "UTF-8")).mkString("&")
    val signature = sign(oauthBaseString)

    "OAuth " + (oauthHeaderParams :+ (("oauth_signature", signature))).map{case (k, v) => s"""$k="$v""""}.mkString(", ")
  }

}
