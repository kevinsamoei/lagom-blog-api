package com.kevin.blog.impl

import com.kevin.blog.api.BlogPost
import play.api.libs.json.Json

case class BlogState(post: option[BlogPost])

object BlogState {
  implicit val format: Format[BlogState] = Json.format
}