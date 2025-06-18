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
<tr><td colspan='2' title='Magma' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Magma.html'><font color='#0000a0'>Magma</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/Magma.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
Magma -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.Group
Group[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='Group' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Group.html'><font color='#0000a0'>Group</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/Group.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x inverse</td><td title='special elements' href=''>u</td></tr><tr><td colspan='3' title='TrivialGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.html'><font color='#0000a0'>TrivialGroup C₁</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.java'>📖</td></tr><tr><td colspan='3' title='KleinGroup' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.html'><font color='#0000a0'>KleinGroup V</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.java'>📖</td></tr><tr><td colspan='3' title='Integers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.html'><font color='#0000a0'>ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'>📖</td></tr><tr><td colspan='3' title='DihedralGroup' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dihedral/DihedralGroup.html'><font color='#0000a0'>D₃</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dihedral/DihedralGroup.java'>📖</td></tr><tr><td colspan='3' title='ProductGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/product/ProductGroup.html'><font color='#0000a0'>V⨯V</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/product/ProductGroup.java'>📖</td></tr></table>
>
]
Group -> {Magma}


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='AdditiveSemiGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.html'><font color='#0000a0'>AdditiveSemiGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
AdditiveSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='MultiplicativeSemiGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.html'><font color='#0000a0'>MultiplicativeSemiGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td></tr></table>
>
]
MultiplicativeSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='MultiplicativeGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.html'><font color='#0000a0'>MultiplicativeGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr><tr><td colspan='4' title='PermutationGroup' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.html'><font color='#0000a0'>PermutationGroup</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.java'>📖</td></tr><tr><td colspan='4' title='GeneralLinearGroup' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.html'><font color='#0000a0'>GL₂(ℚ)</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.java'>📖</td></tr></table>
>
]
MultiplicativeGroup -> {MultiplicativeMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='AdditiveGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.html'><font color='#0000a0'>AdditiveGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x -x</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveGroup -> {AdditiveMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup
AdditiveAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='AdditiveAbelianSemiGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.html'><font color='#0000a0'>AdditiveAbelianSemiGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr><tr><td colspan='3' title='NegativeIntegers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.html'><font color='#0000a0'>NegativeIntegers ℕ⁻</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.java'>📖</td></tr></table>
>
]
AdditiveAbelianSemiGroup -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='AdditiveMonoid' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.html'><font color='#0000a0'>AdditiveMonoid</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td><td title='special elements' href=''>0</td></tr><tr><td colspan='4' title='StringMonoid' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.html'><font color='#0000a0'>StringMonoid</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.java'>📖</td></tr><tr><td colspan='4' title='NaturalNumbers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.html'><font color='#0000a0'>NaturalNumbers ℕ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.java'>📖</td></tr></table>
>
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='MultiplicativeMonoid' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.html'><font color='#0000a0'>MultiplicativeMonoid</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td><td title='special elements' href=''>1</td></tr><tr><td colspan='4' title='OddIntegers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.html'><font color='#0000a0'>OddIntegers ℕo</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'>📖</td></tr></table>
>
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='Rng' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Rng.html'><font color='#0000a0'>Rng</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/Rng.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>0 u</td></tr><tr><td colspan='5' title='EvenIntegers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.html'><font color='#0000a0'>EvenIntegers 2ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.java'>📖</td></tr><tr><td colspan='5' title='NDivisibleIntegers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.html'><font color='#0000a0'>3ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.java'>📖</td></tr></table>
>
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='3' title='MultiplicativeAbelianSemiGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.html'><font color='#0000a0'>MultiplicativeAbelianSemiGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x²</td></tr><tr><td colspan='3' title='OddIntegers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.html'><font color='#0000a0'>OddIntegers ℕo</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'>📖</td></tr></table>
>
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='MultiplicativeAbelianGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.html'><font color='#0000a0'>MultiplicativeAbelianGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='DivisionRing' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.html'><font color='#0000a0'>DivisionRing</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x²</td><td title='special elements' href=''>0 u 1</td></tr><tr><td colspan='5' title='Quaternions' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.html'><font color='#0000a0'>ℍ(ℚ)</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.java'>📖</td></tr></table>
>
]
DivisionRing -> {MultiplicativeGroup
MultiplicativeMonoid
Ring}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='AdditiveAbelianGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.html'><font color='#0000a0'>AdditiveAbelianGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x -x</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveAbelianGroup -> {AdditiveGroup
AdditiveAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='Ring' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Ring.html'><font color='#0000a0'>Ring</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/Ring.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>1 0 u</td></tr><tr><td colspan='5' title='TrivialRing' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.html'><font color='#0000a0'>TrivialRing {0}</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.java'>📖</td></tr><tr><td colspan='5' title='Integers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.html'><font color='#0000a0'>ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'>📖</td></tr></table>
>
]
Ring -> {Rng
MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='AbelianRing' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.html'><font color='#0000a0'>AbelianRing</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>0 u 1</td></tr><tr><td colspan='5' title='ModuloRing' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloRing.html'><font color='#0000a0'>ℤ/8ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloRing.java'>📖</td></tr></table>
>
]
AbelianRing -> {Ring
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisibleGroup
DivisibleGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='4' title='DivisibleGroup' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.html'><font color='#0000a0'>DivisibleGroup</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x x⁻¹ inverse x²</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
DivisibleGroup -> {MultiplicativeAbelianGroup}


# interface org.meeuw.math.abstractalgebra.Field
Field[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='Field' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Field.html'><font color='#0000a0'>Field</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/Field.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x²</td><td title='special elements' href=''>0 u 1</td></tr><tr><td colspan='5' title='GaussianRationals' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.html'><font color='#0000a0'>GaussianRationals 𝐐(i)</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.java'>📖</td></tr><tr><td colspan='5' title='ModuloField' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.html'><font color='#0000a0'>ℤ/pℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'>📖</td></tr><tr><td colspan='5' title='ModuloField' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.html'><font color='#0000a0'>ℤ/3ℤ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'>📖</td></tr></table>
>
]
Field -> {DivisionRing
AbelianRing
DivisibleGroup}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='VectorSpace' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.html'><font color='#0000a0'>VectorSpace</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+x -x inverse x²</td><td title='special elements' href=''>u 1 0</td></tr><tr><td colspan='5' title='Vector3Space' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.html'><font color='#0000a0'>Vector3Space ℝₚ³</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.java'>📖</td></tr><tr><td colspan='5' title='Vector2Space' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim2/Vector2Space.html'><font color='#0000a0'>Vector2Space ℝₚ³</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim2/Vector2Space.java'>📖</td></tr><tr><td colspan='5' title='NVectorSpace' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.html'><font color='#0000a0'>ℚ³</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.java'>📖</td></tr></table>
>
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='CompleteField' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.html'><font color='#0000a0'>CompleteField</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² √x sin cos exp ln sinh cosh</td><td title='special elements' href=''>0 u 1 𝜑 𝜋 ℯ</td></tr><tr><td colspan='5' title='BigComplexNumbers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/BigComplexNumbers.html'><font color='#0000a0'>BigComplexNumbers ℂ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/BigComplexNumbers.java'>📖</td></tr><tr><td colspan='5' title='ComplexNumbers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/ComplexNumbers.html'><font color='#0000a0'>ComplexNumbers ℂₚ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/ComplexNumbers.java'>📖</td></tr></table>
>
]
CompleteField -> {Field}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='ScalarField' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.html'><font color='#0000a0'>ScalarField</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1</td></tr><tr><td colspan='5' title='RationalNumbers' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.html'><font color='#0000a0'>RationalNumbers ℚ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.java'>📖</td></tr></table>
>
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteScalarField
CompleteScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='5' title='CompleteScalarField' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.html'><font color='#0000a0'>CompleteScalarField</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.java'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+x -x x⁻¹ inverse x² √x sin cos exp ln sinh cosh |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1 𝜑 𝜋 ℯ</td></tr><tr><td colspan='5' title='BigDecimalField' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/BigDecimalField.html'><font color='#0000a0'>BigDecimalField ℝ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/BigDecimalField.java'>📖</td></tr><tr><td colspan='5' title='RealField' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/RealField.html'><font color='#0000a0'>RealField ℝₚ</font></td><td href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/reals/RealField.java'>📖</td></tr></table>
>
]
CompleteScalarField -> {CompleteField
ScalarField}


# interface org.meeuw.math.abstractalgebra.AlgebraicStructure
AlgebraicStructure[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td colspan='1' title='AlgebraicStructure' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.html'><font color='#0000a0'>AlgebraicStructure</font></td><td href='MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.java'>📖</td></tr><tr><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+x</td></tr></table>
>
]
}
