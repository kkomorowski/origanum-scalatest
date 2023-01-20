package io.github.kkomorowski.origanum.scalatest

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.tagobjects.Slow

class GivenWhenThenSpec extends AnyFeatureSpec with GivenWhenThen {

  Feature("Feature One") {
    Scenario("Scenario One Failing", Slow) {
      Given("this")
      And("that")
      When("something happen")
      Then("this is expected")
      assert(false, "Here is a fail!")
    }
    Scenario("Scenario Two Passing") {
      assert(true)
    }
  }
}
