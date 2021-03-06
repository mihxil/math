package org.meeuw.math.abstractalgebra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
class AlgebraicStructureTest {
    private static class E implements  AlgebraicElement<E> {

        @Override
        public AlgebraicStructure<E> getStructure() {
            return new S();
        }
    }
    private static class S implements  AlgebraicStructure<E> {

        @Override
        public Cardinality getCardinality() {
            return Cardinality.ALEPH_0;
        }

        @Override
        public Class<E> getElementClass() {
            return E.class;
        }
    }

    private final S s = new S();
    @Test
    public void test() {
        assertThat(s.getSupportedOperators()).isEmpty();
        assertThat(s.getSupportedUnaryOperators()).isEmpty();
        assertThat(s.getDescription()).isEqualTo("S");
        assertThat(s.getEquivalence().test(new E(), new E())).isFalse();
    }


}
