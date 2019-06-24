package list

import qq.droste._
import qq.droste.data._
import qq.droste.syntax.all._
import cats._
import cats.implicits._

sealed trait ListF[A, B]

object ListF {
  // Data structure
  final case class NilF[A, B]()                  extends ListF[A, B]
  final case class ConsF[A, B](head: A, tail: B) extends ListF[A, B]

  // Fixed points
  type Fixed[A] = Fix[ListF[A, ?]]

  object Fixed {
    def wrap[A](a: A): Fixed[A]                    = ConsF(a, NilF().fix).fix
    def cons[A](head: A, tail: Fixed[A]): Fixed[A] = ConsF(head, tail).fix
  }

  //Functor
  implicit def lisFFunctor[A]: Functor[ListF[A, ?]] = new Functor[ListF[A, ?]] {
    def map[B, C](fa: ListF[A, B])(f: B => C): ListF[A, C] = fa match {
      case NilF()            => NilF()
      case ConsF(head, tail) => ConsF(head, f(tail))
    }
  }

  // Cata F[A] => A

  def toListAlgebra[A]: Algebra[ListF[A, ?], List[A]] = Algebra {
    case NilF()            => Nil
    case ConsF(head, tail) => head :: tail
  }

  def toList[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] =
    scheme.cata(toListAlgebra[A]).apply(b)

  def showAlgebra[A: Show]: Algebra[ListF[A, ?], String] = Algebra {
    case NilF()            => "."
    case ConsF(head, tail) => s"${head.show} :: $tail"
  }

  def monoidAlgebra[A: Monoid]: Algebra[ListF[A, ?], A] = Algebra {
    case NilF()      => Monoid[A].empty
    case ConsF(h, t) => Monoid[A].combine(h, t)
  }

  // Combining algebras

  def twoAlgebras[A: Show: Monoid, B](b: B)(implicit B: Basis[ListF[A, ?], B]): (A, String) =
    scheme.cata(monoidAlgebra[A].zip(showAlgebra[A])).apply(b)

  // Ana A => F[A]

  def fromListCoalgebra[A]: Coalgebra[ListF[A, ?], List[A]] = Coalgebra {
    case Nil     => NilF()
    case x :: xs => ConsF(x, xs)
  }

  def fromList[A, B](list: List[A])(implicit B: Embed[ListF[A, ?], B]): B =
    scheme.ana(fromListCoalgebra[A]).apply(list)

  def factorialCoalgebra: Coalgebra[ListF[Int, ?], Int] = Coalgebra {
    case 0 => NilF()
    case n => ConsF(n, n - 1)
  }

  def factorial[B](n: Int)(implicit B: Embed[ListF[Int, ?], B]): B =
    scheme.ana(factorialCoalgebra).apply(n)

  def repeatCoalgebra(x: Int): Coalgebra[ListF[Int, ?], Int] = Coalgebra {
    case 0 => NilF()
    case n => ConsF(x, n - 1)
  }

  def fill[B](n: Int, times: Int)(implicit B: Embed[ListF[Int, ?], B]): B =
    scheme.ana(repeatCoalgebra(n)).apply(times)

  // Hylo = Cata + Ana

  def same[A] = scheme.hylo(toListAlgebra[A], fromListCoalgebra[A])

  def reverseAlgebra[A]: Algebra[ListF[A, ?], List[A]] = Algebra {
    case NilF()            => Nil
    case ConsF(head, tail) => tail :+ head
  }
  def factorialH = scheme.hylo(reverseAlgebra[Int], factorialCoalgebra)

  // Para: F[(R, A)] => A
  def tailAlgebra[A]: RAlgebra[List[A], ListF[A, ?], List[A]] = RAlgebra {
    case NilF()            => Nil
    case ConsF(_, (xs, _)) => xs
  }

  def tail[A](xs: List[A])(implicit B: Project[ListF[A, ?], List[A]]): List[A] =
    scheme.zoo.para(tailAlgebra[A]).apply(xs)

  def slidingAlgebra[A](n: Int): RAlgebra[List[A], ListF[A, ?], List[List[A]]] = RAlgebra {
    case NilF()            => Nil
    case ConsF(x, (xs, r)) => (x :: xs).take(n) :: r
  }
  def sliding[A](n: Int)(xs: List[A])(implicit B: Project[ListF[A, ?], List[A]]): List[List[A]] =
    scheme.zoo.para(slidingAlgebra[A](n)).apply(xs)

  // Apo: A => F[Either[R, A]]
  def mapHeadCoalgebra[A](f: A => A): RCoalgebra[List[A], ListF[A, ?], List[A]] = RCoalgebra {
    case Nil     => NilF()
    case x :: xs => ConsF(f(x), Left(xs))
  }

  def mapHead[A](f: A => A)(xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]): List[A] =
    scheme.zoo.apo(mapHeadCoalgebra[A](f)).apply(xs)

  def insertElementCoalgebra[A: Order]: RCoalgebra[List[A], ListF[A, ?], List[A]] = RCoalgebra {
    case Nil                    => NilF()
    case x :: Nil               => ConsF(x, Left(Nil))
    case x :: y :: xs if x <= y => ConsF(x, Left(y :: xs))
    case x :: y :: xs           => ConsF(y, Right(x :: xs))
  }
  def knockback[A: Order](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]) =
    scheme.zoo.apo(insertElementCoalgebra[A]).apply(xs)

  def sort[A: Order, B](xs: List[A])(implicit B: Embed[ListF[A, ?], List[A]]): List[A] =
    scheme
      .hylo(Algebra[ListF[A, ?], List[A]] {
        case NilF()       => Nil
        case ConsF(x, xs) => knockback(x :: xs)
      }, fromListCoalgebra[A])
      .apply(xs)

  // Histo : F[Attr[F, A]] => A
  def oddsAlgebra[A]: CVAlgebra[ListF[A, ?], List[A]] =
    CVAlgebra {
      case NilF()                           => Nil
      case ConsF(h, _ :<(NilF()))           => List(h)
      case ConsF(h, _ :<(ConsF(_, t :< _))) => h :: t
    }

  def odds[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] =
    scheme.zoo.histo(oddsAlgebra[A]).apply(b)

  def evensAlgebra[A]: CVAlgebra[ListF[A, ?], List[A]] = CVAlgebra {
    case NilF()                           => Nil
    case ConsF(_, _ :<(NilF()))           => Nil
    case ConsF(_, _ :<(ConsF(h, t :< _))) => h :: t
  }

  def evens[A, B](b: B)(implicit B: Basis[ListF[A, ?], B]): List[A] =
    scheme.zoo.histo(evensAlgebra[A]).apply(b)

}

object Example {
  import ListF._
  import Fixed._

  implicit def basis[A]: Basis[ListF[A, ?], List[A]] = Basis.Default(toListAlgebra[A], fromListCoalgebra[A])

  val listF1 = cons(1, wrap(2))
  val listF2 = cons(4, cons(3, cons(2, wrap(1))))
  val to     = listF2
  val from   = List(3, 1, 1, 2, 4, 3, 5, 1, 6, 2, 1)

  val listResult             = toList(to)
  val twoAlgebrasResult      = twoAlgebras[Int, Fix[ListF[Int, ?]]](to)
  val factorialResult        = factorial(10)
  val fillResult             = fill(3, times = 10)
  val listFResult            = fromList[Int, Fix[ListF[Int, ?]]](from)
  val sameResult             = same(List(1, 2, 3))
  val reverseFactorialResult = factorialH(10)
  val tailResult             = tail(List(1, 2, 3, 4))
  val slideResult            = sliding(3)((1 to 5).toList)
  val mapHeadResult          = mapHead[Int](x => x + 1)(List(1, 2, 3, 4))
  val sorted                 = sort(from)
  val oddPosition            = odds((1 to 10).toList)
  val evenPosition           = evens((1 to 10).toList)

  lazy val result = odds(listF2)
}
