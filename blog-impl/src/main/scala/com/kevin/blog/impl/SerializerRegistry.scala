package com.kevin.blog.impl

import com.kevin.blog.Post
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object SerializerRegistry extends JsonSerializerRegistry{
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Post],
    JsonSerializer[BlogState],
    JsonSerializer[PostCreated],
    JsonSerializer[PostUpdated],
    JsonSerializer[PostDeleted],
    JsonSerializer[AcceptedResult],
    JsonSerializer[RejectedResult],
  )
}
