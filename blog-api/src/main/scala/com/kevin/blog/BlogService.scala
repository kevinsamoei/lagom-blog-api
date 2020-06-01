package com.kevin.blog

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api._
import com.lightbend.lagom.scaladsl.api.transport.Method

trait BlogService extends Service {

  def getPost(postId: String): ServiceCall[NotUsed, Post]
  def createPost: ServiceCall[Post, Post]
  def updatePost(postId: String): ServiceCall[Post, Post]
  def deletePost(postId: String): ServiceCall[NotUsed, Post]

  override def descriptor: Descriptor = {
    import Service._

    named("blog").withCalls(
      restCall(Method.GET, "/api/post/:postId", getPost _),
      restCall(Method.POST, "/api/post", createPost _),
      restCall(Method.PUT, "/api/post/:postId", updatePost _),
      restCall(Method.DELETE, "/api/post/:postId", deletePost _),
    )
  }
}
