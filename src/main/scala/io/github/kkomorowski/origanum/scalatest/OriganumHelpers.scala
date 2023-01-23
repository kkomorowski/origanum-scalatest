package io.github.kkomorowski.origanum.scalatest

import org.scalatest.events.{AlertProvided, DiscoveryCompleted, DiscoveryStarting, Event, ExceptionalEvent, InfoProvided, MarkupProvided, NoteProvided, NotificationEvent, RecordableEvent, RunAborted, RunCompleted, RunStarting, RunStopped, ScopeClosed, ScopeOpened, ScopePending, SuiteAborted, SuiteCompleted, SuiteStarting, TestCanceled, TestFailed, TestIgnored, TestPending, TestStarting, TestSucceeded}

import java.util.UUID

private [scalatest] object OriganumHelpers {

  sealed trait OriganumTestResult {
    val suiteId: String
    val testName: String
    val executionTimeMillis: Long
    def testId: UUID = UUID.nameUUIDFromBytes((suiteId + testName).getBytes)
    def toJson: String
  }

  final case class OriganumTestSucceeded(suiteId: String, testName: String, executionTimeMillis: Long) extends OriganumTestResult {
    def toJson: String =
      s"""{"testId":"$testId","status":"SUCCESS","suiteId":"$suiteId","testName":"$testName","executionTimeMillis":$executionTimeMillis}"""
  }

  final case class OriganumTestFailed(suiteId: String, testName: String, executionTimeMillis: Long, reason: Option[String]) extends OriganumTestResult {
    def toJson: String =
      s"""{"testId":"$testId","status":"FAILED","suiteId":"$suiteId","testName":"$testName","executionTimeMillis":$executionTimeMillis,"reason":"${reason.getOrElse("")}"}"""
  }
  final case class OriganumTestIgnored(suiteId: String, testName: String, executionTimeMillis: Long) extends OriganumTestResult {
    def toJson: String =
      s"""{"testId":"$testId","status":"IGNORED","suiteId":"$suiteId","testName":"$testName","executionTimeMillis":$executionTimeMillis}"""
  }
  final case class OriganumTestPending(suiteId: String, testName: String, executionTimeMillis: Long) extends OriganumTestResult {
    def toJson: String =
      s"""{"testId":"$testId","status":"PENDING","suiteId":"$suiteId","testName":"$testName","executionTimeMillis":$executionTimeMillis}"""
  }
  final case class OriganumTestCanceled(suiteId: String, testName: String, executionTimeMillis: Long) extends OriganumTestResult {
    def toJson: String =
      s"""{"testId":"$testId","status":"CANCELED","suiteId":"$suiteId","testName":"$testName","executionTimeMillis":$executionTimeMillis}"""
  }

  def origanumTestSucceeded(start: TestStarting, status: TestSucceeded): OriganumTestSucceeded =
    OriganumTestSucceeded(start.suiteId, start.testName, status.timeStamp - start.timeStamp)

  def origanumTestFailed(start: TestStarting, status: TestFailed): OriganumTestFailed =
    OriganumTestFailed(start.suiteId, start.testName, status.timeStamp - start.timeStamp, status.throwable.map(_.getMessage))

  def origanumTestIgnored(start: TestStarting, status: TestIgnored): OriganumTestIgnored =
    OriganumTestIgnored(start.suiteId, start.testName, status.timeStamp - start.timeStamp)

  def origanumTestPending(start: TestStarting, status: TestPending): OriganumTestPending =
    OriganumTestPending(start.suiteId, start.testName, status.timeStamp - start.timeStamp)

  def origanumTestCancelled(start: TestStarting, status: TestCanceled): OriganumTestCanceled =
    OriganumTestCanceled(start.suiteId, start.testName, status.timeStamp - start.timeStamp)

}
