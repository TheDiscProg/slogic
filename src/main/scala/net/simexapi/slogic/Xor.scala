package net.simexapi.slogic

trait Xor[A, B] {

  def fold[C](fa: A => C, fb: B => C): C

  def foreach[U](fa: A => Unit, fb: B => Unit): Unit

  def flatMap[A1, B1](fa: A => Xor[A1, B1], fb: B => Xor[A1, B1]): Xor[A1, B1]

  def getLeft: Option[A]

  def getRight: Option[B]

  def isLeft: Boolean

  def isRight: Boolean

  def map[A1, B1](fa: A => A1, fb: B => B1): Xor[A1, B1]

  def mapLeft[A1](f: A => A1): Xor[A1, B]

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
