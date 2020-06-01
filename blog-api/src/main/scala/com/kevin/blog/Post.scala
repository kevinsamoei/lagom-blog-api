package com.kevin.blog

import play.api.libs.json.{Format, Json}

case class Post(
    postId: String = "",
    title: String = "",
    body: String = "",
    isDeleted: Boolean = false,
//    createdAt: Instant = ,
//    authors: Seq[String] = Seq.empty
)

object Post {
  implicit val format: Format[Post] = Json.format
}
