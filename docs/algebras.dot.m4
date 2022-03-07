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



# interface org.meeuw.math.abstractalgebra.Magma
Magma[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='1' title='Magma' href='MATH_URL/org/meeuw/math/abstractalgebra/Magma.java'><font color='#0000a0'>Magma</font></td></tr><tr><td> *</td></tr></table>
>
]
Magma -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.Group
Group[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='2' title='Group' href='MATH_URL/org/meeuw/math/abstractalgebra/Group.java'><font color='#0000a0'>Group</font></td></tr><tr><td> *</td><td>u</td></tr><tr><td colspan='2' title='Integers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'><font color='#0000a0'>ℤ</font></td></tr><tr><td colspan='2' title='KleinGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.java'><font color='#0000a0'>V</font></td></tr></table>
>
]
Group -> {Magma}


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='2' title='AdditiveSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java'><font color='#0000a0'>AdditiveSemiGroup</font></td></tr><tr><td> + </td><td> *</td></tr></table>
>
]
AdditiveSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='2' title='MultiplicativeSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java'><font color='#0000a0'>MultiplicativeSemiGroup</font></td></tr><tr><td> ⋅ </td><td> *</td></tr></table>
>
]
MultiplicativeSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='MultiplicativeGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java'><font color='#0000a0'>MultiplicativeGroup</font></td></tr><tr><td> ⋅  / </td><td> *</td><td>1</td><td>u</td></tr><tr><td colspan='4' title='PermutationGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.java'><font color='#0000a0'>PermutationGroup</font></td></tr><tr><td colspan='4' title='GeneralLinearGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.java'><font color='#0000a0'>GL₂(ℚ)</font></td></tr></table>
>
]
MultiplicativeGroup -> {Group
MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='AdditiveGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java'><font color='#0000a0'>AdditiveGroup</font></td></tr><tr><td> +  - </td><td> *</td><td>0</td><td>u</td></tr></table>
>
]
AdditiveGroup -> {Group
AdditiveMonoid}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup
AdditiveAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='2' title='AdditiveAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.java'><font color='#0000a0'>AdditiveAbelianSemiGroup</font></td></tr><tr><td> + <br />⇆</td><td> *</td></tr><tr><td colspan='2' title='NegativeIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.java'><font color='#0000a0'>NegativeIntegers ℕ⁻</font></td></tr></table>
>
]
AdditiveAbelianSemiGroup -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='AdditiveMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java'><font color='#0000a0'>AdditiveMonoid</font></td></tr><tr><td> + </td><td> *</td><td>0</td></tr><tr><td colspan='3' title='StringMonoid' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.java'><font color='#0000a0'>StringMonoid</font></td></tr><tr><td colspan='3' title='NaturalNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.java'><font color='#0000a0'>NaturalNumbers ℕ</font></td></tr></table>
>
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='MultiplicativeMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java'><font color='#0000a0'>MultiplicativeMonoid</font></td></tr><tr><td> ⋅ </td><td> *</td><td>1</td></tr><tr><td colspan='3' title='OddIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td></tr></table>
>
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='Rng' href='MATH_URL/org/meeuw/math/abstractalgebra/Rng.java'><font color='#0000a0'>Rng</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅ </td><td> *</td><td>0</td><td>u</td></tr><tr><td colspan='5' title='EvenIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.java'><font color='#0000a0'>EvenIntegers 2ℤ</font></td></tr><tr><td colspan='5' title='NDivisibleIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.java'><font color='#0000a0'>3ℤ</font></td></tr></table>
>
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='2' title='MultiplicativeAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java'><font color='#0000a0'>MultiplicativeAbelianSemiGroup</font></td></tr><tr><td> ⋅ <br />⇆</td><td> *</td></tr><tr><td colspan='2' title='OddIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td></tr></table>
>
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='MultiplicativeAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java'><font color='#0000a0'>MultiplicativeAbelianGroup</font></td></tr><tr><td> ⋅  / <br />⇆</td><td> *</td><td>1</td><td>u</td></tr></table>
>
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='DivisionRing' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.java'><font color='#0000a0'>DivisionRing</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅  / </td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='Quaternions' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.java'><font color='#0000a0'>ℍ(ℚ)</font></td></tr></table>
>
]
DivisionRing -> {Ring
MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='AdditiveAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java'><font color='#0000a0'>AdditiveAbelianGroup</font></td></tr><tr><td> +  - <br />⇆</td><td> *</td><td>0</td><td>u</td></tr></table>
>
]
AdditiveAbelianGroup -> {AdditiveGroup
AdditiveAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='Ring' href='MATH_URL/org/meeuw/math/abstractalgebra/Ring.java'><font color='#0000a0'>Ring</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅ </td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='ModuloStructure' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloStructure.java'><font color='#0000a0'>ModuloStructure</font></td></tr><tr><td colspan='6' title='Integers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'><font color='#0000a0'>ℤ</font></td></tr></table>
>
]
Ring -> {Rng}


# interface org.meeuw.math.abstractalgebra.DivisibleGroup
DivisibleGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='DivisibleGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.java'><font color='#0000a0'>DivisibleGroup</font></td></tr><tr><td> ⋅  / <br />⇆</td><td> *</td><td>1</td><td>u</td></tr></table>
>
]
DivisibleGroup -> {MultiplicativeAbelianGroup}


# interface org.meeuw.math.abstractalgebra.Field
Field[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='Field' href='MATH_URL/org/meeuw/math/abstractalgebra/Field.java'><font color='#0000a0'>Field</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅  / <br />⇆</td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='ModuloField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/nℤ</font></td></tr><tr><td colspan='6' title='GaussianRationals' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.java'><font color='#0000a0'>GaussianRationals 𝐐(i)</font></td></tr><tr><td colspan='6' title='ModuloField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/3ℤ</font></td></tr></table>
>
]
Field -> {DivisionRing
DivisibleGroup
AbelianRing}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='AbelianRing' href='MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.java'><font color='#0000a0'>AbelianRing</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅ <br />⇆</td><td> *</td><td>0</td><td>1</td><td>u</td></tr></table>
>
]
AbelianRing -> {Ring}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='ScalarField' href='MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.java'><font color='#0000a0'>ScalarField</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅  / <br />⇆</td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='RationalNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.java'><font color='#0000a0'>RationalNumbers ℚ</font></td></tr></table>
>
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='VectorSpace' href='MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.java'><font color='#0000a0'>VectorSpace</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅ <br />⇆</td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='Vector3Space' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.java'><font color='#0000a0'>Vector3Space ℝₚ³</font></td></tr><tr><td colspan='6' title='NVectorSpace' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.java'><font color='#0000a0'>ℚ³</font></td></tr></table>
>
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='6' title='CompleteField' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.java'><font color='#0000a0'>CompleteField</font></td></tr><tr><td> +  - <br />⇆</td><td> ⋅  / <br />⇆</td><td> *</td><td>0</td><td>1</td><td>u</td></tr><tr><td colspan='6' title='BigDecimalField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/BigDecimalField.java'><font color='#0000a0'>BigDecimalField ℝ</font></td></tr><tr><td colspan='6' title='RealField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/RealField.java'><font color='#0000a0'>RealField ℝₚ</font></td></tr></table>
>
]
CompleteField -> {ScalarField}


# interface org.meeuw.math.abstractalgebra.AlgebraicStructure
AlgebraicStructure[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='1' title='AlgebraicStructure' href='MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.java'><font color='#0000a0'>AlgebraicStructure</font></td></tr></table>
>
]
AlgebraicStructure -> {}
}
