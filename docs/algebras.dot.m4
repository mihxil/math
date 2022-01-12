digraph {
    node [
		  shape=record
    ]
		edge [
		  arrowhead = "empty"
		]

        define(BLOB_URL, https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java)


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/AdditiveSemiGroup.java"
		label="{\N|{ + }}"
]
AdditiveSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/MultiplicativeSemiGroup.java"
		label="{\N|{ * }}"
]
MultiplicativeSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
href="BLOB_URL/org/meeuw/math/abstractalgebra/AdditiveMonoid.java"
		label="{\N|{ +  | 0}|StringMonoid}"
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
href="BLOB_URL/org/meeuw/math/abstractalgebra/MultiplicativeMonoid.java"
		label="{\N|{ *  | 1}|OddIntegers}"
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
href="BLOB_URL/org/meeuw/math/abstractalgebra/Rng.java"
		label="{\N|{ +  - \n⇆ |  *  | 0}|EvenIntegers}"
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianSemiGroup
MultiplicativeAbelianSemiGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianSemiGroup.java"
		label="{\N|{ * \n⇆}|OddIntegers}"
]
MultiplicativeAbelianSemiGroup -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/AdditiveGroup.java"
		label="{\N|{ +  -  | 0}}"
]
AdditiveGroup -> {AdditiveMonoid}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/MultiplicativeGroup.java"
		label="{\N|{ *  /  | 1}|PermutationGroup}"
]
MultiplicativeGroup -> {MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
href="BLOB_URL/org/meeuw/math/abstractalgebra/Ring.java"
		label="{\N|{ +  - \n⇆ |  *  | 0 | 1}|ModuloStructure}"
]
Ring -> {Rng}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/AdditiveAbelianGroup.java"
		label="{\N|{ +  - \n⇆ | 0}}"
]
AdditiveAbelianGroup -> {AdditiveGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
href="BLOB_URL/org/meeuw/math/abstractalgebra/MultiplicativeAbelianGroup.java"
		label="{\N|{ *  / \n⇆ | 1}}"
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
href="BLOB_URL/org/meeuw/math/abstractalgebra/DivisionRing.java"
		label="{\N|{ +  - \n⇆ |  *  /  | 0 | 1}|Quaternions}"
]
DivisionRing -> {Ring
MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
href="BLOB_URL/org/meeuw/math/abstractalgebra/AbelianRing.java"
		label="{\N|{ +  - \n⇆ |  * \n⇆ | 0 | 1}}"
]
AbelianRing -> {Ring}


# interface org.meeuw.math.abstractalgebra.Field
Field[
href="BLOB_URL/org/meeuw/math/abstractalgebra/Field.java"
		label="{\N|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|ModuloField\nGaussianRationals}"
]
Field -> {DivisionRing
MultiplicativeAbelianGroup
AbelianRing}


# interface org.meeuw.math.abstractalgebra.VectorSpace
VectorSpace[
href="BLOB_URL/org/meeuw/math/abstractalgebra/VectorSpace.java"
		label="{\N|{ +  - \n⇆ |  * \n⇆ | 0 | 1}|Vector3Space}"
]
VectorSpace -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
href="BLOB_URL/org/meeuw/math/abstractalgebra/ScalarField.java"
		label="{\N|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|RationalNumbers}"
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
href="BLOB_URL/org/meeuw/math/abstractalgebra/CompleteField.java"
		label="{\N|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|BigDecimalField\nRealField}"
]
CompleteField -> {ScalarField}
}
