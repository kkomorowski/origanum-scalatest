package io.github.kkomorowski.origanum.scalatest

import org.scalatest.wordspec.AnyWordSpec

class WordSpecTest extends AnyWordSpec {

  "A Book" can {
    "electronic" should {
      "have page size 0" in {
        assert(true)
      }
      "have a format" in {
        assert(false)
      }
    }
    "audiobook" should {
      "have page size 0" in {
        assert(true)
      }
      "throw an exception" in {
        throw new RuntimeException("This should be thrown here!")
      }
      "this is a pending test" in {
        pending
      }
      "this test is canceled" in {
        assume(false)
      }
    }
  }

}
