/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.abstractalgebra.complex;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.MultiplicativeSemiGroupElement;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumber;
import org.meeuw.math.abstractalgebra.complex.BigComplexNumbers;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.theories.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQRT;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;


/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Log4j2
class BigComplexNumberTest implements
    CompleteFieldTheory<BigComplexNumber>,
    MetricSpaceTheory<BigComplexNumber, BigDecimalElement>,
    WithScalarTheory<BigComplexNumber, BigDecimalElement> {

    static final BigComplexNumbers structure = BigComplexNumbers.INSTANCE;

    @Test
    public void isMultiplicativeSemiGroupElement() {
        BigComplexNumber cn = BigComplexNumber.of(of(1), of(1));
        assertThat(cn).isInstanceOf(MultiplicativeSemiGroupElement.class);
    }
    @Test
    public void testOf() {
        assertThat(BigComplexNumber.of(BigDecimalElement.ONE)).isEqualTo(BigComplexNumber.of(BigDecimalElement.ONE, BigDecimalElement.ZERO));
    }

    @Test
    public void sqrt() {
        BigComplexNumber a = BigComplexNumber.of("0.93 - 0.24i");
        BigComplexNumber s = a.sqrt();
        log.info("{} = {}", SQRT.stringify(a), s);
        assertThat(s).isEqualTo(
            BigComplexNumber.of("0.9722316173666970657425912469437875253699116940133041752687870355356957277102731598795197032865742360 - 0.1234273786785721682678705937060281380123926735849942471609379264448153198764290187034913427341540813i")
        );


    }


    @Test
    public void tetration() {
        withLooseEquals(() -> {
            BigComplexNumber i = BigComplexNumber.of( "i");
            assertThat(i.tetration(1)).isEqualTo(i);
            assertThat(i.tetration(2)).isEqualTo(
                BigComplexNumber.of(".2078795763507619085469556198349787700338778416317696080751358830554198772854821397886002778654260353")
            );
            assertThat(i.tetration(3)).isEqualTo(
                BigComplexNumber.of("0.9471589980723783806534753520181933335039061339031493636713681179446929279300488084526262684626490224 + 0.3207644499793085346601168458748631401023670206812767998296571687407552221593630018130863397275275956i")
            );
        });


        withLooseEquals(() -> {
            BigComplexNumber mi = BigComplexNumber.of( "-i");
            assertThat(mi.tetration(2)).isEqualTo(
                BigComplexNumber.of(".2078795763507619085469556198349787700338778416317696080751358830554198772854821397886002778654260353")
            );
            assertThat(mi.tetration(3)).isEqualTo(
                BigComplexNumber.of("0.9471589980723783806534753520181933335039061339031493636713681179446929279300488084526262684626490224 - 0.3207644499793085346601168458748631401023670206812767998296571687407552221593630018130863397275275956i")
            );
            assertThat(mi.tetration(4)).isEqualTo(
                BigComplexNumber.of(".050092236109321075325057857667404946468201913766929482251191903794255108466570245223267516389354812588592387618207418313011412671583091915593513930380985112553213035843274365368782823138559170316757475 - 0.60211652703600378785675053054092402757580126795341617146912039064405965761446533166149876455748874894091972947073733788769473802085944138985142758564968985377506548603564612677376823835793273631198015i")
            );
        });
    }

    @Test
    public void asini() {
        withLooseEquals(() -> {
            BigComplexNumber i = BigComplexNumber.of("i");
            assertThat(i.asin()).isEqualTo(
                BigComplexNumber.of("0.8813735870195430252326093249797923090281603282616354107532956086533771842220260878337068919102560422i")
            );
            // assertThat(i.asin().sin()).isEqualTo(i); TODO
        });
    }

    @Test
    public void asinexample() {
        withLooseEquals(() -> {
            BigComplexNumber ex = BigComplexNumber.of("0.4 + 0.3i");
            assertThat(ex.asin()).isEqualTo(
                BigComplexNumber.of("0.3903162045220236919955233192246937426417507847073594611232342921938896698631709352163426813930629989 + 0.3189624333048183974533632421390216733671896809815027761489118643037886558460986645968922599086008903i")
            );
            assertThat(ex.asin().sin()).isEqualTo(ex);
        });
    }

    @Test
    public void asinreal() {
        withLooseEquals(() -> {
            BigComplexNumber ex = BigComplexNumber.of("1");
            assertThat(ex.asin()).isEqualTo(
                BigComplexNumber.of(Utils.PI).dividedBy(2)
            );
        });
    }


    @Override
    public Arbitrary<BigComplexNumber> elements() {
        Arbitrary<Double> real = Arbitraries.doubles().between(-100, 100);
        Arbitrary<Double> imaginary = Arbitraries.doubles().between(-100, 100);

        return Combinators.combine(real, imaginary).as((r, i)
            ->
                BigComplexNumber.of(
                    of(r),
                    of(i))
            )
            .edgeCases(config -> {
                config.add(structure.i());
                config.add(structure.one());
                config.add(structure.zero());
            })
            ;
    }

    @Override
    public Arbitrary<BigDecimalElement> scalars() {
        return Arbitraries.of(
            of(0),
            of(1), of(2), of(-1)
        );
    }

}
