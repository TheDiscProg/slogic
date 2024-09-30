package io.github.thediscprog.slogic

/**
 * A simple logical Exclusive OR Container for Scala.
 *
 * `Xor` can only contain one type or the other, not both nor neither - it must contain one and only one type.
 * Both left hand side and right hand side values are of equal importance (a departure from `Either[A, B]` where
 * the RHS value holds the required value and LHS the error).
 *
 * @tparam A
 * @tparam B
 */
sealed trait Xor[A, B] {

  /**
   * Folds a Xor to a single type, C
   *
   * @param fa - function A => C
   * @param fb - function B => C
   * @tparam C - return type C after applying one of the functions
   * @return - the return
   */
  def fold[C](fa: A => C, fb: B => C): C

  /**
   * Applies side effect to the held value
   *
   * @param fa - function if it holds LHS
   * @param fb - function if it holds RHS
   * @tparam U - Unit
   */
  def foreach[U](fa: A => Unit, fb: B => Unit): Unit

  /**
   * FlatMaps Xor with the supplied functions
   *
   * @param fa - function A => A1 if it holds LHS
   * @param fb - function B => B1 if it holds RHS
   * @tparam A1 - return type A1
   * @tparam B1 - return type B1
   * @return - a Xor[A1, B1]
   */
  def flatMap[A1, B1](fa: A => Xor[A1, B1], fb: B => Xor[A1, B1]): Xor[A1, B1]

  /**
   * Gets the LHS value
   *
   * @return Option of A if LHS, otherwise None
   */
  def getLeft: Option[A]

  /**
   * Gets the RHS value
   *
   * @return Option of B if RHS, otherwise None
   */
  def getRight: Option[B]

  /**
   * Checks if it holds LHS
   *
   * @return true if LHS, otherwise false
   */
  def isLeft: Boolean

  /**
   * Checks if it holds RHS
   *
   * @return true if RHS, otherwisse false
   */
  def isRight: Boolean

  /**
   * Maps a Xor by applying the appropriate function
   * It will preserve the hand side, i.e. if LHS, the new object will also be LHS
   *
   * @param fa - function A => A1 if LHS
   * @param fb - function B => B1 if RHS
   * @tparam A1 - return type A1
   * @tparam B1 - return type B1
   * @return - a Xor[A1, B1] after applying one of the above
   */
  def map[A1, B1](fa: A => A1, fb: B => B1): Xor[A1, B1]

  /**
   * Applies a function to LHS
   * @param f - the function A => A1
   * @tparam A1 - the type A1
   * @return a Xor[A1, B] if LHS, otherwise it throws a runtime exception
   */
  def mapLeft[A1](f: A => A1): Xor[A1, B]

  /**
   * Applies a function to RHS
   *
   * @param f - the function B => B1
   * @tparam B1 - the type B1
   * @return a Xor[A, B1] if RHS, otherwise it throws a runtime exception
   */
  def mapRight[B1](f: B => B1): Xor[A, B1]
}

private class XorImpl[A, B] private (private val a: Option[A], private val b: Option[B])
    extends Xor[A, B] {

  override def fold[C](fa: A => C, fb: B => C): C = applyF(fa, fb)

  override def foreach[U](fa: A => Unit, fb: B => Unit): Unit = applyF(fa, fb)

  override def flatMap[A1, B1](fa: A => Xor[A1, B1], fb: B => Xor[A1, B1]): Xor[A1, B1] =
    applyF(fa, fb)

  override def getLeft: Option[A] = a

  override def getRight: Option[B] = b

  override def isLeft: Boolean = a.isDefined

  override def isRight: Boolean = b.isDefined

  override def map[A1, B1](fa: A => A1, fb: B => B1): Xor[A1, B1] =
    if (isLeft)
      Xor.applyLeft(applyLeft(fa))
    else
      Xor.applyRight(applyRight(fb))

  override def mapLeft[A1](f: A => A1): Xor[A1, B] =
    XorImpl.applyLeft(applyLeft(f))

  override def mapRight[B1](f: B => B1): Xor[A, B1] =
    XorImpl.applyRight(applyRight(f))

  /*
   * The methods below help with applying logical operations, such as equals,
   * to this class.
   */

  def canEqual(obj: Any): Boolean = obj.isInstanceOf[XorImpl[_, _]]

  override def equals(obj: Any): Boolean =
    obj match {
      case xor: Xor[_, _] =>
        if (isLeft)
          this.getLeft == xor.getLeft
        else
          this.getRight == xor.getRight
      case _ => false
    }

  override def hashCode(): Int = (a, b).##

  override def toString: String =
    if (isLeft)
      s"Xor(Left[\"${getLeft}\"])"
    else
      s"Xor(Right[\"${getRight}\"])"

  private def applyF[C](fa: A => C, fb: B => C): C =
    if (isLeft)
      applyLeft(fa)
    else
      applyRight(fb)

  private def applyLeft[C](fa: A => C): C =
    a.fold(throw new RuntimeException("XOR error - LHS should be defined but it isn't"))(fa)

  private def applyRight[C](fb: B => C): C =
    b.fold(throw new RuntimeException("XOR error - RHS value should be defined but it isn't"))(fb)

}

private object XorImpl {

  def applyLeft[A, B](a: A): Xor[A, B] = new XorImpl[A, B](Some(a), None)

  def applyRight[A, B](b: B): Xor[A, B] = new XorImpl[A, B](None, Some(b))
}

object Xor {

  def applyLeft[A, B](a: A): Xor[A, B] = XorImpl.applyLeft(a)

  def applyRight[A, B](b: B): Xor[A, B] = XorImpl.applyRight(b)

}
