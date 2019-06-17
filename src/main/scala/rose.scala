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

  def fromNextCoalgebra[A, B]: Coalgebra[RoseF[B, ?], Next[A, B]] = ???

  def fromNext[A, B, C](next: Next[A, B])(implicit C: Embed[RoseF[B, ?], C]): C = ???
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
}
