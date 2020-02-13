package com.kevin.blog.impl

import com.kevin.blog.api.BlogPost
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.Json

sealed trait BlogEvent extends AggregateEvent[BlogEvent] {
  override val aggregateTag = BlogEvent.tag
}

object BlogEvent {
  val Tag: AggregateEvent[BlogEvent] = AggregateEventTag[BlogEvent]
}

case class PostCreated(post: BlogPost) extends BlogEvent
object PostCreated {
  implicit val format: Format[PostCreated] = Json.Format
}

case class PostUpdated(post: BlogPost) extends BlogEvent
object PostUpdated {
  implicit val format: Format[PostUpdated] = Json.Format
}

case class PostDeleted(post: BlogPost) extends BlogEvent
object PostDeleted {
  implicit val format: Format[PostDeleted] = Json.Format
}
