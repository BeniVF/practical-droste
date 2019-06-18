package list

import qq.droste._
import qq.droste.data._
import qq.droste.syntax.all._
import cats._
import cats.implicits._

sealed trait ListF[A, B]

object ListF {
  // Data structure

  // Fixed points
  type Fixed[A] = Nothing

  object Fixed {
    def wrap[A](a: A): Fixed[A]                    = ???
    def cons[A](head: A, tail: Fixed[A]): Fixed[A] = ???
  }

  //Functor
  implicit def lisFFunctor[A]: Functor[ListF[A, ?]] = ???

  // Cata F[A] => A

  def toListAlgebra[A]: Algebra[ListF[A, ?], List[A]] = ???

  def toList[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] = ???

  def showAlgebra[A: Show]: Algebra[ListF[A, ?], String] = ???

  def monoidAlgebra[A: Monoid]: Algebra[ListF[A, ?], A] = ???

  // Combining algebras

  def twoAlgebras[A: Show: Monoid, B](b: B)(implicit B: Basis[ListF[A, ?], B]): (A, String) = ???

  // Ana A => F[A]

  def fromListCoalgebra[A]: Coalgebra[ListF[A, ?], List[A]] = ???

  def fromList[A, B](list: List[A])(implicit B: Embed[ListF[A, ?], B]): B = ???

  def factorialCoalgebra: Coalgebra[ListF[Int, ?], Int] = ???

  def factorial[B](n: Int)(implicit B: Embed[ListF[Int, ?], B]): B = ???

  // Hylo = Cata + Ana

  def same[A] = ???

  def reverseAlgebra[A]: Algebra[ListF[A, ?], List[A]] = ???
  def factorialH[A]                                    = ???

  // Para: F[(R, A)] => A
  def slidingAlgebra[A](n: Int): RAlgebra[List[A], ListF[A, ?], List[List[A]]] = ???
  def sliding[A](n: Int)(xs: List[A])(implicit B: Project[ListF[A, ?], List[A]]): List[List[A]] =
    ???

  // Apo: A => F[Either[R, A]]
  def insertElementCoalgebra[A: Order]: RCoalgebra[List[A], ListF[A, ?], List[A]]      = ???
  def knockback[A: Order](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]])        = ???
  def sort[A: Order, B](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]): List[A] = ???

  // Histo : F[Attr[F, A]] => A
  def oddsAlgebra[A]: CVAlgebra[ListF[A, ?], List[A]]              = ???
  def odds[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] = ???

}

object Example {
  import ListF._
  import Fixed._

  val result = ???

}
