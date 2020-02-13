package com.kevin.blog.impl

import com.kevin.blog.api.BlogPost
import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

sealed trait BlogCommand[R] extends ReplyType[R]

case class CreatePost(post: BlogPost) extends BlogCommand[Done]
object CreatePost{
  implicit val format: Format[CreatePost] = Json.format
}

case class UpdatePost(post: BlogPost) extends BlogCommand[Done]
object UpdatePost{
  implicit val format: Format[UpdatePost] = Json.format
}

case class DeletePost(post: BlogPost) extends BlogCommand[Done]
object DeletePost{
  implicit val format: Format[DeletePost] = Json.format
}

case class GetPost(post: BlogPost) extends BlogCommand[Done]
object GetPost{
  implicit val format: Format[GetPost] = Json.format
}
