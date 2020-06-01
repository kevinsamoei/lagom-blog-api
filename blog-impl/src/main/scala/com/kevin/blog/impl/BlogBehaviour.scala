package com.kevin.blog.impl

import akka.actor.typed.Behavior
import akka.cluster.sharding.typed.scaladsl.EntityContext
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.scaladsl.RetentionCriteria
import com.lightbend.lagom.scaladsl.persistence.AkkaTaggerAdapter

object BlogBehaviour {

  def create(
      entityContext: EntityContext[BlogCommand]
  ): Behavior[BlogCommand] = {
    val persistenceId: PersistenceId =
      PersistenceId(entityContext.entityTypeKey.name, entityContext.entityId)

    create(persistenceId)
      .withTagger(
        AkkaTaggerAdapter.fromLagom(entityContext, BlogEvent.Tag)
      )
      // snapshot every 2 events and keep at most 2 snapshots on db
      .withRetention(
        RetentionCriteria
          .snapshotEvery(numberOfEvents = 2, keepNSnapshots = 2)
      )
  }

  private def create(persistenceId: PersistenceId) =
    EventSourcedBehavior
      .withEnforcedReplies[
        BlogCommand,
        BlogEvent,
        BlogState
      ](
        persistenceId = persistenceId,
        emptyState = BlogState.initial,
        commandHandler = (state, cmd) => state.handleCommand(cmd),
        eventHandler = (state, evt) => state.handleEvent(evt)
      )
}
