package net.simexapi.slogic

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class XorTest extends AnyFlatSpec with Matchers {

  val xorA: Xor[Int, String] = Xor.applyLeft(1)
  val xorB: Xor[Int, String] = Xor.applyRight("one")

  val increment: Int => Int = _ + 1
  val strIncrement: String => String = s => s"$s plus one"

  val printInt: Int => Unit = i => println(s"$i")
  val printStr: String => Unit = s => println(s"$s")

  it should "handle equality" in {
    xorA == Xor.applyLeft(1) shouldBe true
    xorB == Xor.applyRight("one") shouldBe true
  }

  it should "fold using supplied functions" in {
    val r1 = xorA.fold(increment, strIncrement)
    val r2 = xorB.fold(increment, strIncrement)

    r1 shouldBe 2
    r2 shouldBe "one plus one"
  }

  it should "map the correct function" in {
    val r1 = xorA.map(increment, strIncrement)
    val r2 = xorB.map(increment, strIncrement)

    r1 shouldBe Xor.applyLeft(2)
    r2 shouldBe Xor.applyRight("one plus one")
  }

  it should "flatMap the correct function" in {
    val r1 = xorA.flatMap(
      i => Xor.applyLeft(i + 2): Xor[Int, String],
      s => Xor.applyRight(s"$s + one"): Xor[Int, String]
    )
    val r2 = xorB.flatMap(
      i => Xor.applyLeft(i + 2): Xor[Int, String],
      s => Xor.applyRight(s"$s + two"): Xor[Int, String]
    )

    r1 shouldBe Xor.applyLeft(3)
    r2 shouldBe Xor.applyRight("one + two")
  }
}
