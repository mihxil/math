digraph {
    node [
		  shape=record
    ]
		edge [
		  arrowhead = "empty"
		]

        define(URL, https://github.com/mihxil/math/blob/main/mihxil-math/src/main/java/)


# interface org.meeuw.math.abstractalgebra.AdditiveSemiGroup
AdditiveSemiGroup[
href="URL"
		label="{AdditiveSemiGroup|{ + }}"
]
AdditiveSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.MultiplicativeSemiGroup
MultiplicativeSemiGroup[
href="URL"
		label="{MultiplicativeSemiGroup|{ * }}"
]
MultiplicativeSemiGroup -> {AlgebraicStructure}


# interface org.meeuw.math.abstractalgebra.AdditiveMonoid
AdditiveMonoid[
href="URL"
		label="{AdditiveMonoid|{ +  | 0}|StringMonoid}"
]
AdditiveMonoid -> {AdditiveSemiGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeMonoid
MultiplicativeMonoid[
href="URL"
		label="{MultiplicativeMonoid|{ *  | 1}|OddIntegers}"
]
MultiplicativeMonoid -> {MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.Rng
Rng[
href="URL"
		label="{Rng|{ +  - \n⇆ |  *  | 0}|EvenIntegers}"
]
Rng -> {AdditiveAbelianGroup
MultiplicativeSemiGroup}


# interface org.meeuw.math.abstractalgebra.AdditiveGroup
AdditiveGroup[
href="URL"
		label="{AdditiveGroup|{ +  -  | 0}}"
]
AdditiveGroup -> {AdditiveMonoid}


# interface org.meeuw.math.abstractalgebra.MultiplicativeGroup
MultiplicativeGroup[
href="URL"
		label="{MultiplicativeGroup|{ *  /  | 1}|PermutationGroup}"
]
MultiplicativeGroup -> {MultiplicativeMonoid}


# interface org.meeuw.math.abstractalgebra.Ring
Ring[
href="URL"
		label="{Ring|{ +  - \n⇆ |  *  | 0 | 1}|ModuloStructure}"
]
Ring -> {Rng}


# interface org.meeuw.math.abstractalgebra.AdditiveAbelianGroup
AdditiveAbelianGroup[
href="URL"
		label="{AdditiveAbelianGroup|{ +  - \n⇆ | 0}}"
]
AdditiveAbelianGroup -> {AdditiveGroup}


# interface org.meeuw.math.abstractalgebra.MultiplicativeAbelianGroup
MultiplicativeAbelianGroup[
href="URL"
		label="{MultiplicativeAbelianGroup|{ *  / \n⇆ | 1}}"
]
MultiplicativeAbelianGroup -> {MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.DivisionRing
DivisionRing[
href="URL"
		label="{DivisionRing|{ +  - \n⇆ |  *  /  | 0 | 1}|Quaternions}"
]
DivisionRing -> {Ring
MultiplicativeGroup}


# interface org.meeuw.math.abstractalgebra.AbelianRing
AbelianRing[
href="URL"
		label="{AbelianRing|{ +  - \n⇆ |  * \n⇆ | 0 | 1}}"
]
AbelianRing -> {Ring}


# interface org.meeuw.math.abstractalgebra.Field
Field[
href="URL"
		label="{Field|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|ModuloField\nGaussianRationals}"
]
Field -> {DivisionRing
MultiplicativeAbelianGroup
AbelianRing}


# interface org.meeuw.math.abstractalgebra.VectorSpaceInterface
VectorSpaceInterface[
href="URL"
		label="{VectorSpaceInterface|{ +  - \n⇆ |  * \n⇆ | 0 | 1}|Vector3Space}"
]
VectorSpaceInterface -> {AbelianRing}


# interface org.meeuw.math.abstractalgebra.ScalarField
ScalarField[
href="URL"
		label="{ScalarField|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|RationalNumbers}"
]
ScalarField -> {Field}


# interface org.meeuw.math.abstractalgebra.CompleteField
CompleteField[
href="URL"
		label="{CompleteField|{ +  - \n⇆ |  *  / \n⇆ | 0 | 1}|RealField}"
]
CompleteField -> {ScalarField}
}
