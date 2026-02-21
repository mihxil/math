digraph {
    node [
      shape=plain
    ]
    edge [
      arrowhead = "empty"
    ]
    define(`MATH_URL', https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java)
    define(`ALGEBRA_URL', https://github.com/mihxil/math/blob/main/mihxil-algebra/src/main/java)
    define(`JAVADOC_MATH_URL', https://javadoc.io/doc/org.meeuw.math/mihxil-math/latest/org.meeuw.math)
    define(`JAVADOC_ALGEBRA_URL', https://javadoc.io/doc/org.meeuw.math/mihxil-algebra/latest/org.meeuw.math.algebras)
    changecom(`  #')


# interface org.meeuw.math.abstractalgebra.Magma
Magma[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='2' title='Magma' href='MATH_URL/org/meeuw/math/abstractalgebra/Magma.java'><font color='#0000a0'>Magma</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Magma.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+</td></tr></table>
>
]
Magma -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.Group
Group[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='Group' href='MATH_URL/org/meeuw/math/abstractalgebra/Group.java'><font color='#0000a0'>Group</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Group.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+ inverse</td><td title='special elements' href=''>u</td></tr><tr><td  colspan='3' title='TrivialGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.java'><font color='#0000a0'>TrivialGroup C₁</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/TrivialGroup.html'>📖</td></tr><tr><td  colspan='3' title='KleinGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.java'><font color='#0000a0'>KleinGroup V</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/klein/KleinGroup.html'>📖</td></tr><tr><td  colspan='3' title='Integers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'><font color='#0000a0'>ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.html'>📖</td></tr><tr><td  colspan='3' title='dihedral symmetry group representing the symmetries of a regular 3-gon' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dihedral/DihedralGroup.java'><font color='#0000a0'>D₃</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dihedral/DihedralGroup.html'>📖</td></tr><tr><td  colspan='3' title='ProductGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/product/ProductGroup.java'><font color='#0000a0'>V⨯V</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/product/ProductGroup.html'>📖</td></tr></table>
>
]
Group -> {Magma}


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='AdditiveSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java'><font color='#0000a0'>AdditiveSemiGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+</td></tr></table>
>
]
AdditiveSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='MultiplicativeSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java'><font color='#0000a0'>MultiplicativeSemiGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ sqr</td></tr></table>
>
]
MultiplicativeSemiGroup -> {Magma}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java'><font color='#0000a0'>MultiplicativeGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ reciprocal inverse sqr</td><td title='special elements' href=''>1 u</td></tr><tr><td  colspan='4' title='PermutationGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.java'><font color='#0000a0'>PermutationGroup</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/permutations/PermutationGroup.html'>📖</td></tr><tr><td  colspan='4' title='GeneralLinearGroup' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.java'><font color='#0000a0'>GL₂(ℚ)</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/linear/GeneralLinearGroup.html'>📖</td></tr></table>
>
]
MultiplicativeGroup -> {MultiplicativeMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java'><font color='#0000a0'>AdditiveGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+ -</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveGroup -> {AdditiveMonoid
Group}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup
AdditiveAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='AdditiveAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.java'><font color='#0000a0'>AdditiveAbelianSemiGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+</td></tr><tr><td  colspan='3' title='NegativeIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.java'><font color='#0000a0'>NegativeIntegers ℕ⁻</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NegativeIntegers.html'>📖</td></tr></table>
>
]
AdditiveAbelianSemiGroup -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java'><font color='#0000a0'>AdditiveMonoid</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+</td><td title='special elements' href=''>0</td></tr><tr><td  colspan='4' title='StringMonoid' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.java'><font color='#0000a0'>StringMonoid</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/strings/StringMonoid.html'>📖</td></tr><tr><td  colspan='4' title='NaturalNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.java'><font color='#0000a0'>NaturalNumbers ℕ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NaturalNumbers.html'>📖</td></tr></table>
>
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeMonoid' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java'><font color='#0000a0'>MultiplicativeMonoid</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ sqr</td><td title='special elements' href=''>1</td></tr><tr><td  colspan='4' title='OddIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.html'>📖</td></tr><tr><td  colspan='4' title='Squares' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Squares.java'><font color='#0000a0'>Squares ℤ²</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Squares.html'>📖</td></tr></table>
>
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Rng' href='MATH_URL/org/meeuw/math/abstractalgebra/Rng.java'><font color='#0000a0'>Rng</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Rng.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - inverse sqr</td><td title='special elements' href=''>0 u</td></tr><tr><td  colspan='5' title='EvenIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.java'><font color='#0000a0'>EvenIntegers 2ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/EvenIntegers.html'>📖</td></tr><tr><td  colspan='5' title='NDivisibleIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.java'><font color='#0000a0'>3ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/NDivisibleIntegers.html'>📖</td></tr></table>
>
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='3' title='MultiplicativeAbelianSemiGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java'><font color='#0000a0'>MultiplicativeAbelianSemiGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ sqr</td></tr><tr><td  colspan='3' title='OddIntegers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.java'><font color='#0000a0'>OddIntegers ℕo</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/OddIntegers.html'>📖</td></tr></table>
>
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='MultiplicativeAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java'><font color='#0000a0'>MultiplicativeAbelianGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ reciprocal inverse sqr</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='DivisionRing' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.java'><font color='#0000a0'>DivisionRing</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisionRing.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - reciprocal inverse sqr</td><td title='special elements' href=''>0 u 1</td></tr><tr><td  colspan='5' title='Quaternions' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.java'><font color='#0000a0'>ℍ(ℚ)</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/quaternions/Quaternions.html'>📖</td></tr></table>
>
]
DivisionRing -> {MultiplicativeGroup
MultiplicativeMonoid
Ring}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='AdditiveAbelianGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java'><font color='#0000a0'>AdditiveAbelianGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+ -</td><td title='special elements' href=''>0 u</td></tr></table>
>
]
AdditiveAbelianGroup -> {AdditiveGroup
AdditiveAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Ring' href='MATH_URL/org/meeuw/math/abstractalgebra/Ring.java'><font color='#0000a0'>Ring</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Ring.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - inverse sqr</td><td title='special elements' href=''>1 0 u</td></tr><tr><td  colspan='5' title='TrivialRing' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.java'><font color='#0000a0'>TrivialRing {0}</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/TrivialRing.html'>📖</td></tr><tr><td  colspan='5' title='Integers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.java'><font color='#0000a0'>ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/Integers.html'>📖</td></tr></table>
>
]
Ring -> {Rng
MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='AbelianRing' href='MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.java'><font color='#0000a0'>AbelianRing</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AbelianRing.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - inverse sqr</td><td title='special elements' href=''>0 u 1</td></tr><tr><td  colspan='5' title='OddEventRing' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/OddEventRing.java'><font color='#0000a0'>OddEventRing org.meeuw.math.abstractalgebra.trivial.OddEventRing@3efecf77</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/trivial/OddEventRing.html'>📖</td></tr><tr><td  colspan='5' title='The polynomial ring in x over ℚ' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/polynomial/PolynomialRing.java'><font color='#0000a0'>ℚ[x]</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/polynomial/PolynomialRing.html'>📖</td></tr><tr><td  colspan='5' title='The polynomial ring in x over ℤ' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/polynomial/PolynomialRing.java'><font color='#0000a0'>ℤ[x]</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/polynomial/PolynomialRing.html'>📖</td></tr><tr><td  colspan='5' title='ModuloRing' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloRing.java'><font color='#0000a0'>ℤ/8ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloRing.html'>📖</td></tr></table>
>
]
AbelianRing -> {Ring
MultiplicativeAbelianSemiGroup}


# interface org.meeuw.math.abstractalgebra.DivisibleGroup
DivisibleGroup[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='4' title='DivisibleGroup' href='MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.java'><font color='#0000a0'>DivisibleGroup</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/DivisibleGroup.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ reciprocal inverse sqr</td><td title='special elements' href=''>1 u</td></tr></table>
>
]
DivisibleGroup -> {MultiplicativeAbelianGroup}


# interface org.meeuw.math.abstractalgebra.Field
Field[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='Field' href='MATH_URL/org/meeuw/math/abstractalgebra/Field.java'><font color='#0000a0'>Field</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/Field.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - reciprocal inverse sqr</td><td title='special elements' href=''>0 u 1</td></tr><tr><td  colspan='5' title='GaussianRationals' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.java'><font color='#0000a0'>GaussianRationals 𝐐(i)</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/GaussianRationals.html'>📖</td></tr><tr><td  colspan='5' title='ModuloField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/pℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.html'>📖</td></tr><tr><td  colspan='5' title='ModuloField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.java'><font color='#0000a0'>ℤ/3ℤ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/integers/ModuloField.html'>📖</td></tr></table>
>
]
Field -> {DivisionRing
AbelianRing
DivisibleGroup}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='VectorSpace' href='MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.java'><font color='#0000a0'>VectorSpace</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/VectorSpace.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅<br />⇆</td><td title='other binary operators' href=''> ≈   ≉  xⁿ</td><td title='Unary operators' href=''>+ - inverse sqr</td><td title='special elements' href=''>u 1 0</td></tr><tr><td  colspan='5' title='Vector3Space' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.java'><font color='#0000a0'>Vector3Space ℝᵤ³</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim3/Vector3Space.html'>📖</td></tr><tr><td  colspan='5' title='Vector2Space' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim2/Vector2Space.java'><font color='#0000a0'>Vector2Space ℝᵤ³</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/dim2/Vector2Space.html'>📖</td></tr><tr><td  colspan='5' title='NVectorSpace' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.java'><font color='#0000a0'>ℚ³</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/vectorspace/NVectorSpace.html'>📖</td></tr></table>
>
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='CompleteField' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.java'><font color='#0000a0'>CompleteField</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+ - reciprocal inverse sqr sqrt sin cos exp ln sinh cosh</td><td title='special elements' href=''>0 u 1 𝜑 𝜋 ℯ</td></tr><tr><td  colspan='5' title='BigComplexNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/BigComplexNumbers.java'><font color='#0000a0'>BigComplexNumbers ℂ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/BigComplexNumbers.html'>📖</td></tr><tr><td  colspan='5' title='ComplexNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/ComplexNumbers.java'><font color='#0000a0'>ComplexNumbers ℂₚ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/complex/ComplexNumbers.html'>📖</td></tr></table>
>
]
CompleteField -> {Field}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='ScalarField' href='MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.java'><font color='#0000a0'>ScalarField</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/ScalarField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ</td><td title='Unary operators' href=''>+ - reciprocal inverse sqr |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1 𝜋</td></tr><tr><td  colspan='5' title='RationalNumbers' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.java'><font color='#0000a0'>RationalNumbers ℚ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/rationalnumbers/RationalNumbers.html'>📖</td></tr></table>
>
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteScalarField
CompleteScalarField[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='5' title='CompleteScalarField' href='MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.java'><font color='#0000a0'>CompleteScalarField</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/CompleteScalarField.html'>📖</td></tr><tr><td title='group binary operator' href=''> * </td><td title='binary operators of addition' href=''>+-<br />⇆</td><td title='binary operators of multiplication' href=''>⋅/<br />⇆</td><td title='other binary operators' href=''> ^   ≈   ≉   &lt;   ≲   &gt;   ≳  xⁿ ⁿ√x ⁿx</td><td title='Unary operators' href=''>+ - reciprocal inverse sqr sqrt sin cos exp ln sinh cosh |x| x₌ ⌊x⌉</td><td title='special elements' href=''>0 u 1 𝜑 ℯ 𝜋</td></tr><tr><td  colspan='5' title='BigDecimalField' href='ALGEBRA_URL/org/meeuw/math/abstractalgebra/bigdecimals/BigDecimalField.java'><font color='#0000a0'>BigDecimalField ℝ</font></td><td  title='javadoc' href='JAVADOC_ALGEBRA_URL/org/meeuw/math/abstractalgebra/bigdecimals/BigDecimalField.html'>📖</td></tr><tr><td  colspan='5' title='RealField' href='MATH_URL/org/meeuw/math/abstractalgebra/reals/RealField.java'><font color='#0000a0'>RealField ℝᵤ</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/reals/RealField.html'>📖</td></tr></table>
>
]
CompleteScalarField -> {CompleteField
ScalarField}


# interface org.meeuw.math.abstractalgebra.AlgebraicStructure
AlgebraicStructure[
		margin=2	label=<
<table border='0'  cellborder='1' cellspacing='0' cellpadding='1'>
<tr><td  colspan='1' title='AlgebraicStructure' href='MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.java'><font color='#0000a0'>AlgebraicStructure</font></td><td  title='javadoc' href='JAVADOC_MATH_URL/org/meeuw/math/abstractalgebra/AlgebraicStructure.html'>📖</td></tr><tr><td title='other binary operators' href=''> ≈   ≉ </td><td title='Unary operators' href=''>+</td></tr></table>
>
]
}
