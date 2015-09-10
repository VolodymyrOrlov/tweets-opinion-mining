package com.vorlov.helper.format

import com.vorlov.api.twitter.model.{TwitterUser, TweetMetadata, Tweet}
import com.vorlov.util.Csv.CSVFormat
import com.vorlov.util.Date._

package object csv {

  implicit val TweetCsvFormat = new CSVFormat[Tweet] {
    override def write(tweet: Tweet): Iterable[(String, Any)] = Seq(
      ("createdAt" -> tweet.createdAt.iso8601Str),
      ("id" -> tweet.id),
      ("text" -> tweet.text),
      ("source" -> tweet.source),
      ("truncated" -> tweet.truncated),
      ("isQuoteStatus" -> tweet.isQuoteStatus),
      ("retweetCount" -> tweet.retweetCount),
      ("retweeted" -> tweet.retweeted),
      ("metadata.language" -> tweet.language),
      ("metadata.resultType" -> tweet.metadata.resultType),
      ("user.id" -> tweet.user.id),
      ("user.name" -> tweet.user.name),
      ("user.screenName" -> tweet.user.screenName),
      ("user.isProtected" -> tweet.user.isProtected),
      ("user.followersCount" -> tweet.user.followersCount),
      ("user.friendsCount" -> tweet.user.friendsCount),
      ("user.listedCount" -> tweet.user.listedCount),
      ("user.createdAt" -> tweet.user.createdAt.iso8601Str),
      ("user.favouritesCount" -> tweet.user.favouritesCount),
      ("user.language" -> tweet.user.language)
    )

    override def read(cells: Seq[String]): Tweet = {

      val metadata = TweetMetadata(cells(8), cells(9))

      val user = TwitterUser(cells(10).toLong, cells(11), cells(12), cells(13).toBoolean, cells(14).toInt, cells(15).toInt, cells(16).toInt, cells(17).toDateTime, cells(18).toInt, cells(19), None, None, None)

      Tweet(cells(0).toDateTime, cells(1).toLong, cells(2), cells(3), cells(4).toBoolean, cells(5).toBoolean, cells(6).toInt, cells(7).toBoolean, metadata, user, None, None, None, None, None, None)

    }
  }

}
