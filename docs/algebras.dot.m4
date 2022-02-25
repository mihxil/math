digraph {
    node [
		  shape=plain
    ]
		edge [
		  arrowhead = "empty"
		]

        define(`MATH_URL', https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java)
        define(`ALGEBRA_URL', https://github.com/mihxil/math/blob/main/mihxil-algebra/src/main/java)
         changecom(`  #')



# interface org.meeuw.math.abstractalgebra.Group
Group[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/Group.java'><font color='#0000a0'>Group</font></td></tr><tr><td> *</td><td>u</td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.java'><font color='#0000a0'>V</font></td></tr></table>
>
]
Group -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='1' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java'><font color='#0000a0'>AdditiveSemiGroup</font></td></tr><tr><td> + </td></tr></table>
>
]
AdditiveSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='1' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java'><font color='#0000a0'>MultiplicativeSemiGroup</font></td></tr><tr><td> ⋅ </td></tr></table>
>
]
MultiplicativeSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup
AdditiveAbelianSemiGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='1' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.java'><font color='#0000a0'>AdditiveAbelianSemiGroup</font></td></tr><tr><td> + <br />n⇆</td></tr><tr><td colspan='1' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.java'><font color='#0000a0'>NegativeIntegers ℕ⁻</font></td></tr></table>
>
]
AdditiveAbelianSemiGroup -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java'><font color='#0000a0'>AdditiveMonoid</font></td></tr><tr><td> + </td><td>0</td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.java'><font color='#0000a0'>StringMonoid</font></td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.java'><font color='#0000a0'>NaturalNumbers ℕ</font></td></tr></table>
>
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java'><font color='#0000a0'>MultiplicativeMonoid</font></td></tr><tr><td> ⋅ </td><td>1</td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td></tr></table>
>
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='3' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/Rng.java'><font color='#0000a0'>Rng</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅ </td><td>0</td></tr><tr><td colspan='3' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.java'><font color='#0000a0'>EvenIntegers 2ℤ</font></td></tr><tr><td colspan='3' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.java'><font color='#0000a0'>3ℤ</font></td></tr></table>
>
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='1' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java'><font color='#0000a0'>MultiplicativeAbelianSemiGroup</font></td></tr><tr><td> ⋅ <br />⇆</td></tr><tr><td colspan='1' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td></tr></table>
>
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java'><font color='#0000a0'>AdditiveAbelianGroup</font></td></tr><tr><td> +  - <br />n⇆</td><td>0</td></tr></table>
>
]
AdditiveAbelianGroup -> {AdditiveGroup
AdditiveAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java'><font color='#0000a0'>AdditiveGroup</font></td></tr><tr><td> +  - </td><td>0</td></tr></table>
>
]
AdditiveGroup -> {AdditiveMonoid}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java'><font color='#0000a0'>MultiplicativeGroup</font></td></tr><tr><td> ⋅  / </td><td>1</td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.java'><font color='#0000a0'>PermutationGroup</font></td></tr><tr><td colspan='2' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.java'><font color='#0000a0'>GL₂(ℚ)</font></td></tr></table>
>
]
MultiplicativeGroup -> {MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/Ring.java'><font color='#0000a0'>Ring</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅ </td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloStructure.java'><font color='#0000a0'>ModuloStructure</font></td></tr></table>
>
]
Ring -> {Rng}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java'><font color='#0000a0'>MultiplicativeAbelianGroup</font></td></tr><tr><td> ⋅  / <br />⇆</td><td>1</td></tr></table>
>
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.java'><font color='#0000a0'>DivisionRing</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅  / </td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.java'><font color='#0000a0'>ℍ(ℚ)</font></td></tr></table>
>
]
DivisionRing -> {Ring
MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.java'><font color='#0000a0'>AbelianRing</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅ <br />⇆</td><td>0</td><td>1</td></tr></table>
>
]
AbelianRing -> {Ring}


# interface org.meeuw.math.abstractalgebra.DivisibleGroup
DivisibleGroup[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='2' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.java'><font color='#0000a0'>DivisibleGroup</font></td></tr><tr><td> ⋅  / <br />⇆</td><td>1</td></tr></table>
>
]
DivisibleGroup -> {MultiplicativeAbelianGroup}


# interface org.meeuw.math.abstractalgebra.Field
Field[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/Field.java'><font color='#0000a0'>Field</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅  / <br />⇆</td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/nℤ</font></td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.java'><font color='#0000a0'>GaussianRationals ᵁ0(i)</font></td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/3ℤ</font></td></tr></table>
>
]
Field -> {DivisionRing
DivisibleGroup
AbelianRing}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.java'><font color='#0000a0'>VectorSpace</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅ <br />⇆</td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.java'><font color='#0000a0'>Vector3Space ℝₚ³</font></td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.java'><font color='#0000a0'>ℚ³</font></td></tr></table>
>
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.java'><font color='#0000a0'>ScalarField</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅  / <br />⇆</td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.java'><font color='#0000a0'>RationalNumbers ℚ</font></td></tr></table>
>
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
		label=<
<table border='0'  cellborder='1' cellspacing='0'>
<tr><td colspan='4' title='java code' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.java'><font color='#0000a0'>CompleteField</font></td></tr><tr><td> +  - <br />n⇆</td><td> ⋅  / <br />⇆</td><td>0</td><td>1</td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/BigDecimalField.java'><font color='#0000a0'>BigDecimalField ℝ</font></td></tr><tr><td colspan='4' title='java code' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/RealField.java'><font color='#0000a0'>RealField ℝₚ</font></td></tr></table>
>
]
CompleteField -> {ScalarField}
}
