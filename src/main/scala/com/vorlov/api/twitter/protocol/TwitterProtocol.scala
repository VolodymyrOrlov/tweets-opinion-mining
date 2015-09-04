package com.vorlov.api.twitter.protocol

import com.vorlov.api.twitter.model._
import spray.json._

import com.vorlov.util.JSONUtils._
import com.vorlov.util.Date._

object TwitterProtocol extends DefaultJsonProtocol {

  implicit object TwitterUserReader extends RootJsonReader[TwitterUser] {

    def read(value: JsValue) = {

      value.asJsObject.safeGetFields(
        "id",
        "name",
        "screen_name",
        "protected",
        "followers_count",
        "friends_count",
        "listed_count",
        "created_at",
        "favourites_count",
        "lang",
        "location",
        "description",
        "url") match {

        case Seq(
        JsNumber(id),
        JsString(name),
        JsString(screenName),
        JsBoolean(isProtected),
        JsNumber(followersCount),
        JsNumber(friendsCount),
        JsNumber(listedCount),
        JsString(createdAt),
        JsNumber(favouritesCount),
        JsString(language),
        location: JsValue,
        description: JsValue,
        url: JsValue) =>

          TwitterUser(
            id.toLong,
            name,
            screenName,
            isProtected,
            followersCount.toInt,
            friendsCount.toInt,
            listedCount.toInt,
            createdAt.toDateTime,
            favouritesCount.toInt,
            language,
            location.asOptString,
            description.asOptString,
            url.asOptString)

      }

    }
  }

  implicit object TweetMetadataReader extends RootJsonReader[TweetMetadata] {

    def read(value: JsValue) = {

      value.asJsObject.safeGetFields(
        "result_type",
        "iso_language_code") match {

        case Seq(
        JsString(resultType),
        JsString(language)) =>

          TweetMetadata(resultType, language)

      }

    }
  }

  implicit object TweetReader extends RootJsonReader[Tweet] {

    def read(value: JsValue) = {

      value.asJsObject.safeGetFields(
        "created_at",
        "id",
        "text",
        "source",
        "truncated",
        "is_quote_status",
        "retweet_count",
        "retweeted",
        "metadata",
        "user",
        "in_reply_to_status_id",
        "in_reply_to_user_id",
        "in_reply_to_screen_name",
        "favorite_count",
        "possibly_sensitive",
        "lang") match {

        case Seq(
          JsString(created_at),
          JsNumber(id),
          JsString(text),
          JsString(source),
          JsBoolean(truncated),
          JsBoolean(isQuoteStatus),
          JsNumber(retweetCount),
          JsBoolean(retweeted),
          metadata: JsObject,
          user: JsObject,
          inReplyToStatusID: JsValue,
          inReplyToNumberID: JsValue,
          inReplyToScreenName: JsValue,
          favoriteCount: JsValue,
          possiblySensitive: JsValue,
          language: JsValue) =>

          Tweet(created_at.toDateTime,
            id.toLong,
            text,
            source,
            truncated,
            isQuoteStatus,
            retweetCount.toInt,
            retweeted,
            metadata.convertTo[TweetMetadata],
            user.convertTo[TwitterUser],
            inReplyToStatusID.asOptLong,
            inReplyToNumberID.asOptLong,
            inReplyToScreenName.asOptString,
            favoriteCount.asOptInt,
            possiblySensitive.asOptBoolean,
            language.asOptString)

        case s =>
          throw new Exception(s"could not match $s")

      }

    }
  }

  implicit object TwitterSearchResponseMetadataReader extends RootJsonReader[TwitterSearchResponseMetadata] {

    def read(value: JsValue) = {

      value.asJsObject.safeGetFields("completed_in", "max_id", "next_results", "query", "refresh_url", "count", "since_id") match {

        case Seq(JsNumber(completedIn), JsNumber(maxID), nextResults, JsString(query), JsString(refreshURL), JsNumber(count), JsNumber(sinceID)) =>

          TwitterSearchResponseMetadata(completedIn.toDouble, maxID.toLong, nextResults.asOptString, query, refreshURL, count.toInt, sinceID.toInt)

        }

    }
  }

  implicit object TwitterSearchResponseReader extends RootJsonReader[TwitterSearchResponse] {

    def read(value: JsValue) = {

      value.asJsObject.safeGetFields("statuses", "search_metadata") match {

        case Seq(JsArray(statuses), searchMetadata: JsObject) =>

          TwitterSearchResponse(statuses.map(_.convertTo[Tweet]), searchMetadata.convertTo[TwitterSearchResponseMetadata])

      }

    }
  }

}
