package list

sealed trait ListF[A, B]

object ListF {
  import qq.droste._
  import cats._

  implicit def lisFFunctor[A]: Functor[ListF[A, ?]] = ???

  type Fixed[A] = Nothing //TODO Need to be defined
  object Fixed {
    def wrap[A](a: A): Fixed[A]                    = ???
    def cons[A](head: A, tail: Fixed[A]): Fixed[A] = ???
  }

  // Cata F[A] => A

  def toListAlgebra[A]: Algebra[ListF[A, ?], List[A]] = ???

  def toList[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] = ???

  def lengthAlgebra[A]: Algebra[ListF[A, ?], Int] = ???

  def showAlgebra[A: Show]: Algebra[ListF[A, ?], String] = ???

  def monoidAlgebra[A: Monoid]: Algebra[ListF[A, ?], A] = ???

  def filterAlgebra[A](f: A => Boolean): Algebra[ListF[A, ?], List[A]] = ???

  // Combining algebras

  def twoAlgebras[A: Show: Monoid, B](b: B)(implicit B: Basis[ListF[A, ?], B]): (A, String) = ???

  // Ana A => F[A]

  def fromListCoalgebra[A]: Coalgebra[ListF[A, ?], List[A]] = ???

  def fromList[A, B](list: List[A])(implicit B: Embed[ListF[A, ?], B]): B = ???

  def factorialCoalgebra: Coalgebra[ListF[Int, ?], Int] = ???

  def factorial[B](n: Int)(implicit B: Embed[ListF[Int, ?], B]): B = ???

  def repeatCoalgebra(x: Int): Coalgebra[ListF[Int, ?], Int] = ???

  def fill[B](n: Int, times: Int)(implicit B: Embed[ListF[Int, ?], B]): B = ???

  // Hylo = Cata + Ana

  def same[A]               = ???
  def length[A]             = ???
  def combineAll[A: Monoid] = ???

  def reverseAlgebra[A]: Algebra[ListF[A, ?], List[A]] = ???
  def factorialH[A]                                    = ???

  // Para: F[(R, A)] => A
  def tailAlgebra[A]: RAlgebra[List[A], ListF[A, ?], List[A]]                                   = ???
  def tail[A](xs: List[A])(implicit B: Project[ListF[A, ?], List[A]]): List[A]                  = ???
  def slidingAlgebra[A](n: Int): RAlgebra[List[A], ListF[A, ?], List[List[A]]]                  = ???
  def sliding[A](n: Int)(xs: List[A])(implicit B: Project[ListF[A, ?], List[A]]): List[List[A]] = ???

  // Apo: A => F[Either[R, A]]
  def mapHeadCoalgebra[A](f: A => A): RCoalgebra[List[A], ListF[A, ?], List[A]]            = ???
  def mapHead[A](f: A => A)(xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]): List[A] = ???

  def insertElementCoalgebra[A: Order]: RCoalgebra[List[A], ListF[A, ?], List[A]]      = ???
  def knockback[A: Order](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]])        = ???
  def sort[A: Order, B](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]): List[A] = ???

  // Histo : F[Attr[F, A]] => A
  def oddsAlgebra[A]: CVAlgebra[ListF[A, ?], List[A]]              = ???
  def odds[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] = ???

  def evensAlgebra[A]: CVAlgebra[ListF[A, ?], List[A]]              = ???
  def evens[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] = ???

}

object Example {
  import ListF._
  import Fixed._
  import qq.droste._
  import qq.droste.data._
  import cats._
  import cats.implicits._

}
