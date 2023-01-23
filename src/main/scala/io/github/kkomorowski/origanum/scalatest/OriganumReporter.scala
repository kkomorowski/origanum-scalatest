package io.github.kkomorowski.origanum.scalatest

import io.github.kkomorowski.origanum.scalatest.OriganumHelpers.{OriganumTestResult, origanumTestCancelled, origanumTestFailed, origanumTestIgnored, origanumTestPending, origanumTestSucceeded}
import org.scalatest.Reporter
import org.scalatest.events.{Event, SuiteCompleted, TestCanceled, TestFailed, TestIgnored, TestPending, TestStarting, TestSucceeded}
import org.slf4j.LoggerFactory

import java.io.{File, FileWriter}
import scala.collection.mutable.ListBuffer

class OriganumReporter extends Reporter {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val events: ListBuffer[Event] = ListBuffer.empty

  private val targetDir: File = new File("target")
  private val reportDir: File = new File(targetDir, "origanum")

  if (!targetDir.exists()) targetDir.mkdirs()
  if (!reportDir.exists()) reportDir.mkdirs()

  override def apply(event: Event): Unit = {
    events += event
    event match {
      case e: TestSucceeded => handleTestSucceeded(e, events.toList)
      case e: TestFailed    => handleTestFailed(e, events.toList)
      case e: TestIgnored   => handleTestIgnored(e, events.toList)
      case e: TestPending   => handleTestPending(e, events.toList)
      case e: TestCanceled  => handleTestCanceled(e, events.toList)
      case _ =>
    }
  }

  private def writeEvent(testResult: OriganumTestResult): Unit = {
    val file = new File(reportDir, testResult.testId + ".json")
    val fw = new FileWriter(file)
    fw.write(testResult.toJson)
    fw.close()
  }

  private def handleTestSucceeded(e: TestSucceeded, events: List[Event]): Unit = {
    val startEvent = findStartEvent(events, e.suiteId, e.testName)
    val loggedEvent = startEvent.map(origanumTestSucceeded(_, e)).get
    logger.debug(loggedEvent.toJson)
    writeEvent(loggedEvent)
  }

  private def handleTestFailed(e: TestFailed, events: List[Event]): Unit = {
    val startEvent = findStartEvent(events, e.suiteId, e.testName)
    val loggedEvent = startEvent.map(origanumTestFailed(_, e)).get
    logger.debug(loggedEvent.toJson)
    writeEvent(loggedEvent)
  }

  private def handleTestIgnored(e: TestIgnored, events: List[Event]): Unit = {
    val startEvent = findStartEvent(events, e.suiteId, e.testName)
    val loggedEvent = startEvent.map(origanumTestIgnored(_, e)).get
    logger.debug(loggedEvent.toJson)
    writeEvent(loggedEvent)
  }

  private def handleTestPending(e: TestPending, events: List[Event]): Unit = {
    val startEvent = findStartEvent(events, e.suiteId, e.testName)
    val loggedEvent = startEvent.map(origanumTestPending(_, e)).get
    logger.debug(loggedEvent.toJson)
    writeEvent(loggedEvent)
  }

  private def handleTestCanceled(e: TestCanceled, events: List[Event]): Unit = {
    val startEvent = findStartEvent(events, e.suiteId, e.testName)
    val loggedEvent = startEvent.map(origanumTestCancelled(_, e)).get
    logger.debug(loggedEvent.toJson)
    writeEvent(loggedEvent)
  }

  private def findStartEvent(events: List[Event], suiteId: String, testName: String): Option[TestStarting] = {
    events.find {
      case e: TestStarting if e.suiteId == suiteId && e.testName == testName => true
      case _ => false
    }.map(_.asInstanceOf[TestStarting])
  }
}
