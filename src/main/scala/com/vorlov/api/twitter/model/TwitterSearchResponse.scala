package com.vorlov.api.twitter.model

case class TwitterSearchResponseMetadata(completedIn: Double, maxID: Long, nextResults: Option[String], query: String, refreshURL: String, count: Int, sinceID: Int)

case class TwitterSearchResponse(statuses: Seq[Tweet], metadata: TwitterSearchResponseMetadata)
