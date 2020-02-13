package com.kevin.blog.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait BlogService extends Service {
  override final def descriptor: Descriptor = {
    import Service._
    named("blog")
      .withCalls(
        restCall(Method.POST, "/api/post", createPost),
        restCall(Method.PUT, "/api/post/:postId", updatePost _),
        restCall(Method.DELETE, "/api/post/:postId", deletePost _),
        restCall(Method.GET, "/api/post/:postId", getPost _),
        ).withAutoAcl(true)
  }

  def createPost: ServiceCall[BlogPost, BlogPost]
  def updatePost(postId: String): ServiceCall[BlogPost, BlogPost]
  def deletePost(postId: String): ServiceCall[NotUsed, BlogPost]
  def getPost(postId: String): ServiceCall[NotUsed, BlogPost]
}