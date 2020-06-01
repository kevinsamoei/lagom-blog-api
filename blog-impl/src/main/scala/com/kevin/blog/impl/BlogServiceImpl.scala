package com.kevin.blog.impl

import java.util.UUID

import akka.NotUsed
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.EntityRef
import akka.util.Timeout
import com.kevin.blog.BlogService
import com.kevin.blog.Post
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.BadRequest
import com.lightbend.lagom.scaladsl.api.transport.TransportErrorCode
import com.lightbend.lagom.scaladsl.api.transport.TransportException

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class BlogServiceImpl(clusterSharding: ClusterSharding)(implicit ec: ExecutionContext) extends BlogService {

  implicit val timeout: Timeout = Timeout(5.seconds)

  private def entityRef(id: String): EntityRef[BlogCommand] =
    clusterSharding.entityRefFor(BlogState.typeKey, id)

  override def getPost(postId: String): ServiceCall[NotUsed, Post] = ServiceCall { _ =>
    entityRef(postId)
      .ask[CommandResult](reply => GetPost(reply))
      .map {
        case AcceptedResult(post) =>
          post.getOrElse(throw new TransportException(TransportErrorCode.InternalServerError, ""))
        case RejectedResult(reason) => throw BadRequest(reason)
      }

  }

  override def createPost: ServiceCall[Post, Post] = ServiceCall { req =>
    val postId = UUID.randomUUID().toString
    entityRef(postId)
      .ask[CommandResult](reply => CreatePost(req.copy(postId = postId), reply))
      .map {
        case AcceptedResult(post) =>
          post.getOrElse(throw new TransportException(TransportErrorCode.InternalServerError, ""))
        case RejectedResult(reason) => throw BadRequest(reason)
      }
  }

  override def updatePost(postId: String): ServiceCall[Post, Post] = ServiceCall { req =>
    entityRef(postId)
      .ask[CommandResult](reply => UpdatePost(req, reply))
      .map {
        case AcceptedResult(post) =>
          post.getOrElse(throw new TransportException(TransportErrorCode.InternalServerError, ""))
        case RejectedResult(reason) => throw BadRequest(reason)
      }
  }

  override def deletePost(postId: String): ServiceCall[NotUsed, Post] = ServiceCall { _ =>
    entityRef(postId)
      .ask[CommandResult](reply => DeletePost(reply))
      .map {
        case AcceptedResult(post) =>
          post.getOrElse(throw new TransportException(TransportErrorCode.InternalServerError, ""))
        case RejectedResult(reason) => throw BadRequest(reason)
      }
  }
}
