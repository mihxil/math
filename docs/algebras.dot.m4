digraph {
    node [
		  shape=plain
    ]
		edge [
		  arrowhead = "empty"
		]

        define(`MATH_URL', https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java)
        define(`ALGEBRA_URL', https://github.com/mihxil/math/blob/main/mihxil-algebra/src/main/java)
        define(`JAVADOC_MATH_URL', https://www.javadoc.io/doc/org.meeuw.math/mihxil-math/latest/org.meeuw.math)
        define(`JAVADOC_ALGEBRA_URL', https://www.javadoc.io/doc/org.meeuw.math/mihxil-algebra/latest/org.meeuw.math.algebras)
         changecom(`  #')



# interface org.meeuw.math.abstractalgebra.Magma
Magma[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='2' title='Magma' href='MATH_URL/org/meeuw/math/abstractalgebra/Magma.java'><font color='#0000a0'>Magma</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Magma.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
Magma -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.Group
Group[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='Group' href='MATH_URL/org/meeuw/math/abstractalgebra/Group.java'><font color='#0000a0'>Group</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Group.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x inverse</td><td title='special elements' href=''>u</td></tr><tr><td  colspan='3' title='TrivialGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.java'><font color='#0000a0'>TrivialGroup C₁</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.html'>📖</td></tr></table>
>
]
Group -> {Magma}


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='AdditiveSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java'><font color='#0000a0'>AdditiveSemiGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
AdditiveSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='MultiplicativeSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java'><font color='#0000a0'>MultiplicativeSemiGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td></tr></table>
>
]
MultiplicativeSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java'><font color='#0000a0'>MultiplicativeGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
MultiplicativeGroup -> {MultiplicativeMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java'><font color='#0000a0'>AdditiveGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x -x</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveGroup -> {AdditiveMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup
AdditiveAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='AdditiveAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.java'><font color='#0000a0'>AdditiveAbelianSemiGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
AdditiveAbelianSemiGroup -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java'><font color='#0000a0'>AdditiveMonoid</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td><td title='special elements' href=''>0</td></tr></table>
>
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java'><font color='#0000a0'>MultiplicativeMonoid</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td><td title='special elements' href=''>1</td></tr></table>
>
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Rng' href='MATH_URL/org/meeuw/math/abstractalgebra/Rng.java'><font color='#0000a0'>Rng</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Rng.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='MultiplicativeAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java'><font color='#0000a0'>MultiplicativeAbelianSemiGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td></tr></table>
>
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java'><font color='#0000a0'>MultiplicativeAbelianGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='DivisionRing' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.java'><font color='#0000a0'>DivisionRing</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x²</td><td title='special elements' href=''>0 u 1</td></tr></table>
>
]
DivisionRing -> {MultiplicativeGroup
MultiplicativeMonoid
Ring}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java'><font color='#0000a0'>AdditiveAbelianGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x -x</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveAbelianGroup -> {AdditiveGroup
AdditiveAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Ring' href='MATH_URL/org/meeuw/math/abstractalgebra/Ring.java'><font color='#0000a0'>Ring</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Ring.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>1 0 u</td></tr><tr><td  colspan='5' title='TrivialRing' href='MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.java'><font color='#0000a0'>TrivialRing {0}</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.html'>📖</td></tr></table>
>
]
Ring -> {Rng
MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='AbelianRing' href='MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.java'><font color='#0000a0'>AbelianRing</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>0 u 1</td></tr></table>
>
]
AbelianRing -> {Ring
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisibleGroup
DivisibleGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='DivisibleGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.java'><font color='#0000a0'>DivisibleGroup</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
DivisibleGroup -> {MultiplicativeAbelianGroup}


# interface org.meeuw.math.abstractalgebra.Field
Field[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Field' href='MATH_URL/org/meeuw/math/abstractalgebra/Field.java'><font color='#0000a0'>Field</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Field.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x²</td><td title='special elements' href=''>0 u 1</td></tr></table>
>
]
Field -> {DivisionRing
AbelianRing
DivisibleGroup}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='VectorSpace' href='MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.java'><font color='#0000a0'>VectorSpace</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>u 1 0</td></tr></table>
>
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='CompleteField' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.java'><font color='#0000a0'>CompleteField</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² √x sin cos exp ln sinh cosh</td><td title='special elements' href=''>0 u 1 𝜑 ℯ 𝜋</td></tr></table>
>
]
CompleteField -> {Field}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='ScalarField' href='MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.java'><font color='#0000a0'>ScalarField</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1</td></tr></table>
>
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteScalarField
CompleteScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='CompleteScalarField' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.java'><font color='#0000a0'>CompleteScalarField</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² √x sin cos exp ln sinh cosh |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1 𝜑 ℯ 𝜋</td></tr></table>
>
]
CompleteScalarField -> {CompleteField
ScalarField}


# interface org.meeuw.math.abstractalgebra.AlgebraicStructure
AlgebraicStructure[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='1' title='AlgebraicStructure' href='MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.java'><font color='#0000a0'>AlgebraicStructure</font></td><td  target='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.html'>📖</td></tr><tr><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
}
