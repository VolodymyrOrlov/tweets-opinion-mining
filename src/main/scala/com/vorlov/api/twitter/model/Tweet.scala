package com.vorlov.api.twitter.model

import org.joda.time.DateTime

case class TweetMetadata(language: String, resultType: String)

case class Tweet(createdAt: DateTime,
                 id: Long,
                 text: String,
                 source: String,
                 truncated: Boolean,
                 isQuoteStatus: Boolean,
                 retweetCount: Int,
                 retweeted: Boolean,
                 metadata: TweetMetadata,
                 user: TwitterUser,
                 inReplyToStatusID: Option[Long],
                 inReplyToUserID: Option[Long],
                 inReplyToScreenName: Option[String],
                 favoriteCount: Option[Int],
                 possiblySensitive: Option[Boolean],
                 language: Option[String])
