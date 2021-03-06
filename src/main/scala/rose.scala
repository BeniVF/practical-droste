package rose

final case class RoseF[A, B](node: A, forest: List[B])

object RoseF {
  import cats._
  import qq.droste._

  type Next[A, B] = (A, (A => (B, List[A])))

  implicit def roseFunctor[C]: Functor[RoseF[C, ?]] = new Functor[RoseF[C, ?]] {
    def map[A, B](fa: RoseF[C, A])(f: A => B): RoseF[C, B] =
      fa.copy(forest = fa.forest.map(f))
  }

  def depthAlgebra[A]: Algebra[RoseF[A, ?], Int] = Algebra {
    case RoseF(a, Nil)    => 1
    case RoseF(a, forest) => 1 + forest.max
  }

  def depth[A, B](b: B)(implicit B: Basis[RoseF[A, ?], B]): Int =
    scheme.cata(depthAlgebra[A]).apply(b)

  def fromNextCoalgebra[A, B]: Coalgebra[RoseF[A, ?], Next[B, A]] = Coalgebra {
    case (a, f) =>
      val (b, as) = f(a)
      RoseF(b, as.map(_ -> f))
  }

  def fromNext[A, B, C](next: Next[B, A])(implicit C: Embed[RoseF[A, ?], C]): C =
    scheme.ana(fromNextCoalgebra[A, B]).apply(next)
}

object Example {
  import RoseF._

  def next(root: Int): Next[Int, String] =
    (
      root,
      Map(
        1 -> ("first"  -> List(2, 3)),
        2 -> ("second" -> List.empty),
        3 -> ("third"  -> List(4)),
        4 -> ("fourth" -> List.empty)
      ))

  def build(root: Int) = fromNext(next(root))

  def depth(root: Int): Int = RoseF.depth(build(root))
}
