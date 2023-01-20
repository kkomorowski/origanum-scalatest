package io.github.kkomorowski.origanum.scalatest

import org.scalatest.Reporter
import org.scalatest.events.{Event, SuiteCompleted}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

class OriganumReporter extends Reporter {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val events: ListBuffer[Event] = ListBuffer.empty

  private def displayEvents(events: List[Event]): Unit =
    events.foreach(e => println(e.toString + "\n"))

  override def apply(event: Event): Unit =
    event match {
      case e: SuiteCompleted => displayEvents(events.toList.filter(e.))
      case _ => events += event
    }

}
