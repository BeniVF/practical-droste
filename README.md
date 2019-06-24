# Practical Droste

## Algebras

- *Algebra*: `F[A] => A`
- *Coalgebra*: `A => F[A]`
- *AlgebraM*: `F[A] => M[A]`
- *CoalgebraM*: `A => M[F[A]]`
- *RAlgebra*: `F[(R, A)] => A`
- *RCoalgebra* `A => F[Either[R, A]]`
- *RAlgebraM*: `A => M[F[Either[R, A]]]`
- *RCoalgebraM*: `A => M[F[Either[R, A]]]`

## Recursion schemes

| Fold(Recursion)                  | Unfold (Corecursion)                        | General |
| -------------------------------- |:-------------------------------------------:| -------:|
| *cata*                           | *ana*                                       | *hylo*  |
|  `F[A] => A`                     |  `A => F[A]`                                |         |
| *para*                           | *apo*                                       |         |
|  `F[(R,A)] => A`                 |  `A => F[Either[R,A]`                       |         |

## Open console

```sh
 > sh console.sh
```

## References

- [Practical Recursion Schemes](https://medium.com/@jaredtobin/practical-recursion-schemes-c10648ec1c29)
- [Time traveling](https://jtobin.io/time-traveling-recursion)
- [Functional Programming with Bananas Lenses Envelop es and Barb ed Wire](https://maartenfokkinga.github.io/utwente/mmf91m.pdf)
- [AST playground: recursion schemes and recursive data](https://kubuszok.com/2019/ast-playground-recursion-schemes-and-recursive-data/#trampoline-to-stack-safety)
- [Domain Modelling with Haskell: Factoring Out Recursion](https://haskell-at-work.com/episodes/2018-02-11-domain-modelling-with-haskell-factoring-out-recursion.html)
- [Recursion Schemes in Scala - An Absolutely Elementary Introduction](https://free.cofree.io/2017/11/13/recursion/)
- [F-Algebras](https://bartoszmilewski.com/2017/02/28/f-algebras/)
- [Introduction to Recursion Schemes with Matryoshka](https://akmetiuk.com/posts/2017-03-10-matryoshka-intro.html)
- [Awesome Recursion schemes](https://github.com/passy/awesome-recursion-schemes)
- [An Introduction to Recursion Schemes](https://blog.sumtypeofway.com/an-introduction-to-recursion-schemes/)
- [Those 10000 classes I never wrote](http://www.lambdadays.org/static/upload/media/1519663154528176valentinkasasthese10000classesineverwrote.pdf)
- [Sorting slower](https://jtobin.io/sorting-slower-with-style)
- [Time Traveling Recursion Schemes](https://jtobin.io/time-traveling-recursion)
- [Recursion Schemes](https://www.youtube.com/watch?v=Zw9KeP3OzpU)
