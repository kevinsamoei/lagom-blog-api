package com.kevin.blog.impl

import com.kevin.blog.Post
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}


trait BlogEvent extends AggregateEvent[BlogEvent] {
    def aggregateTag: AggregateEventTagger[BlogEvent] = BlogEvent.Tag
}

object BlogEvent {
  val Tag: AggregateEventShards[BlogEvent] = AggregateEventTag.sharded[BlogEvent](10)
}

final case class PostCreated(post: Post) extends BlogEvent

object PostCreated{
  implicit val format: Format[PostCreated] = Json.format
}

final case class PostUpdated(post: Post) extends BlogEvent

object PostUpdated{
  implicit val format: Format[PostUpdated] = Json.format
}

final case class PostDeleted(post: Post) extends BlogEvent

object PostDeleted{
  implicit val format: Format[PostDeleted] = Json.format
}
