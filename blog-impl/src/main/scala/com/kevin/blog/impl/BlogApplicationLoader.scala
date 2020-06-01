package com.kevin.blog.impl

import akka.cluster.sharding.typed.scaladsl.Entity
import com.kevin.blog.BlogService
import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcPersistenceComponents
import com.lightbend.lagom.scaladsl.persistence.slick.SlickPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.LagomApplication
import com.lightbend.lagom.scaladsl.server.LagomApplicationContext
import com.lightbend.lagom.scaladsl.server.LagomApplicationLoader
import com.lightbend.lagom.scaladsl.server.LagomServer
import com.softwaremill.macwire.wire
import play.api.db.HikariCPComponents
import play.api.libs.ws.ahc.AhcWSComponents

class BlogApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new BlogApplication(context) with AkkaDiscoveryComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new BlogApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[BlogService])
}

abstract class BlogApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with SlickPersistenceComponents
    with HikariCPComponents
    with AhcWSComponents
    with LagomKafkaComponents
    with JdbcPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[BlogService](wire[BlogServiceImpl])

  // register json serializer
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = SerializerRegistry

  // initialize sharding
  clusterSharding.init(
    Entity(BlogState.typeKey) { entityContext => BlogBehaviour.create(entityContext) }
  )
}
