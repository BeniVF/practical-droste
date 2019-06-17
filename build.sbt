name := "Practical Droste"
scalaVersion := "2.12.8"

version := "0.0.1-SNAPSHOT"

val V = new {
  val droste           = "0.6.0"
  val kindProjector    = "0.9.9"
  val macroParadise    = "2.1.1"
  val betterMonadicFor = "0.2.4"
}

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-Ypartial-unification"
)

libraryDependencies ++= Seq(
  "io.higherkindness" %% "droste-core"   % V.droste,
  "io.higherkindness" %% "droste-macros" % V.droste,
  ("com.lihaoyi" % "ammonite" % "1.5.0" % "test").cross(CrossVersion.full)
)

scalafmtOnCompile := true
sourceGenerators in Test += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
  Seq(file)
}.taskValue

libraryDependencies ++= Seq(
  compilerPlugin("org.spire-math"  % "kind-projector"      % V.kindProjector cross CrossVersion.binary),
  compilerPlugin("com.olegpy"      %% "better-monadic-for" % V.betterMonadicFor),
  compilerPlugin("org.scalamacros" % "paradise"            % V.macroParadise cross CrossVersion.patch)
)
