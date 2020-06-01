package com.kevin.blog.impl

import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.persistence.typed.scaladsl.{Effect, ReplyEffect}
import com.kevin.blog.Post
import play.api.libs.json.{Format, Json}

case class BlogState(post: Option[Post]){

  // event handlers
  def handlePostAdded(post:Post): BlogState = copy(Some(post))
  def handlePostUpdated(post:Post): BlogState = copy(Some(post))
  def handlePostDeleted(): BlogState = copy(post = post.map(p => p.copy(isDeleted = true)))

  // command handlers
  def handleCreatePost(post: Post, replyTo: ActorRef[CommandResult]): ReplyEffect[BlogEvent, BlogState] = {
    if (this.post.isEmpty) Effect.reply(replyTo)(RejectedResult("invalid command: CreatePost"))
    else Effect.persist(PostCreated(post)).thenReply(replyTo)(updatedState => AcceptedResult(updatedState.post))
  }

  def handleUpdatePost(post: Post, replyTo: ActorRef[CommandResult]): ReplyEffect[BlogEvent, BlogState] = {
    if (this.post.isEmpty) Effect.reply(replyTo)(RejectedResult(s"invalid command: UpdatePost"))
    else Effect.persist(PostUpdated(post)).thenReply(replyTo)(updatedState => AcceptedResult(updatedState.post))
  }

  def handleDeletePost(replyTo: ActorRef[CommandResult]): ReplyEffect[BlogEvent, BlogState] = {
    if (this.post.isEmpty) Effect.reply(replyTo)(RejectedResult(s"invalid command: DeletePost"))
    else Effect.persist(PostDeleted(post.get)).thenReply(replyTo)(updatedState => AcceptedResult(updatedState.post))
  }

  def handleGetPost(replyTo: ActorRef[CommandResult]): ReplyEffect[BlogEvent, BlogState] = {
    Effect.reply(replyTo)(AcceptedResult(post))
  }

  def handleEvent(evt: BlogEvent): BlogState = {
    evt match {
      case PostCreated(post) => handlePostAdded(post)
      case PostDeleted(_) => handlePostDeleted()
      case PostUpdated(post) =>handlePostUpdated(post)
    }
  }

  def handleCommand(cmd: BlogCommand): ReplyEffect[BlogEvent, BlogState] = {
    cmd match {
      case CreatePost(post, replyTo) => handleCreatePost(post, replyTo)
      case DeletePost(replyTo) =>handleDeletePost(replyTo)
      case GetPost(replyTo) =>handleGetPost(replyTo)
      case UpdatePost(post, replyTo) =>handleUpdatePost(post, replyTo)
    }
  }

}

object BlogState {
  def initial: BlogState = BlogState(None)

  val typeKey: EntityTypeKey[BlogCommand] =
    EntityTypeKey[BlogCommand]("Post")


  implicit val format: Format[BlogState] = Json.format
}
