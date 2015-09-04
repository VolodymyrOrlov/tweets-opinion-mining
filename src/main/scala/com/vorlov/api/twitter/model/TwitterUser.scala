package com.vorlov.api.twitter.model

import org.joda.time.DateTime

case class TwitterUser(id: Long,
                       name: String,
                       screenName: String,
                       isProtected: Boolean,
                       followersCount: Int,
                       friendsCount: Int,
                       listedCount: Int,
                       createdAt: DateTime,
                       favouritesCount: Int,
                       language: String,
                       location: Option[String],
                       description: Option[String],
                       url: Option[String])
