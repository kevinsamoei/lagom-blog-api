package com.kevin.blog.api

import java.util.UUID

import play.api.libs.json.{Json, Format}

/**
  * @param ID The id of the post
 *  @param title The title of the blog post
 *  @param body The body of the blog post
 *  @param author The author of the blog post
 *
**/

case class BlogPost(
                 ID: UUID,
                 title: String,
                 body: String,
                 author: String
               )

object BlogPost {
  implicit val format: Format[BlogPost] = Json.format
}