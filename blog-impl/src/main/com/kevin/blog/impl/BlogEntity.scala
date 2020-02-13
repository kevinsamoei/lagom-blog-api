package com.kevin.blog.impl

import akka.Done
import com.kevin.blog.api.BlogPost
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class BlogEntity extends PersistentEntity {
  override type Command = BlogCommand
  override type Event = BlogEvent
  override type State = BlogState

  override def initialState: BlogState =
    BlogState(Set.empty[BlogPost])

  override def behavior: Behavior = {
    case BlogState(posts) =>
      Actions()
      .onCommand(CreatePost, Done) {
        case (CreatePost) =>
      }
  }
}