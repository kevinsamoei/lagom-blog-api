package com.kevin.blog.impl

import akka.actor.typed.ActorRef
import com.kevin.blog.Post
import play.api.libs.json.{Format, Json}

sealed trait SerializableCommand

trait BlogCommand extends SerializableCommand

final case class GetPost(replyTo: ActorRef[CommandResult]) extends BlogCommand
final case class CreatePost(post: Post, replyTo: ActorRef[CommandResult]) extends BlogCommand
final case class UpdatePost(post: Post, replyTo: ActorRef[CommandResult]) extends BlogCommand
final case class DeletePost(replyTo: ActorRef[CommandResult]) extends BlogCommand

trait CommandResult

final case class AcceptedResult(post: Option[Post]) extends CommandResult

final case class RejectedResult(reason: String) extends CommandResult

object RejectedResult {
  implicit val format: Format[RejectedResult] =Json.format
}

object AcceptedResult {
  implicit val format: Format[AcceptedResult] =Json.format
}
