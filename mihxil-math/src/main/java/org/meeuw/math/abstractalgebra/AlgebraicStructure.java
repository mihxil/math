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
package org.meeuw.math.abstractalgebra;

import java.lang.reflect.Array;
import java.util.*;

import java.util.function.Consumer;

import org.meeuw.math.*;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.operators.*;

import static java.util.Collections.unmodifiableNavigableSet;
import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.IDENTIFY;
import static org.meeuw.math.operators.OperatorInterface.COMPARATOR;

/**
 * The base interface of all algebraic structures.
 * <p>
 * If defines what arithmetic {@link BasicAlgebraicBinaryOperator}s are possible its elements
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this structure
 */
public interface AlgebraicStructure<E extends AlgebraicElement<E>> extends Randomizable<E> {

    NavigableSet<AlgebraicComparisonOperator> EQ_ONLY = navigableSet(BasicComparisonOperator.EQ, BasicComparisonOperator.NEQ);

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = unmodifiableNavigableSet(new TreeSet<>(COMPARATOR));

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(IDENTIFY);

    NavigableSet<GenericFunction> FUNCTIONS = Collections.emptyNavigableSet();

    /**
     * Returns the {@link AlgebraicBinaryOperator}s that elements of this structure support.
     * @return the set of all supported binary operators in this algebraic structure
     */
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return Collections.emptyNavigableSet();
    }

    /**
     * Returns the {@link BasicAlgebraicUnaryOperator}s that elements of this structure support.
     * @return the set of all supported unary operators in this algebraic structure
     */
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    /**
     * Returns the {@link AlgebraicComparisonOperator}s that elements of this structure support.
     *
     * @return the set of all supported comparison operators in this algebraic structure
     */
    default NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return EQ_ONLY;
    }

    /**
     * Returns the {@link GenericFunction}s that elements of this structure support.
     * <p>
     * This is a unary operation that works on an algebraic element, which does not necessary return an element of the algebra.
     * <p>
     * E.g. this may represent something like {@link ScalarFieldElement#bigDecimalValue()}. A function shared by many elements of
     * different algebra's, but not related to the algebra itself.
     *
     * @return the set of all supported functions in this algebraic structure
     */
    default NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    /**
     * Gets the direct (known) super groups (or other algebraic structure) which this structure is part of.

     * The associated {@link AlgebraicElement} implements {@link AlgebraicElement#castDirectly(Class)} to support all these classes.
     *
     * @see #getAncestorGroups()
     * @see AlgebraicElement#castDirectly(Class)
     *
     * @return the set of direct super structures of this.
     */
    default Set<AlgebraicStructure<?>> getSuperGroups() {
        return Collections.emptySet();
    }

    /**
     * Calls {@link #getSuperGroups()} recursively.
     * <p>
     * There's normally no need to implement this, as the default should be good.
     *
     * @see #getSuperGroups()
     * @see AlgebraicElement#cast(Class)
     *
     * @return All super groups and ancestors of these
     */
    default Set<AlgebraicStructure<?>> getAncestorGroups() {
        return getAncestorGroups(new HashSet<>());
    }

    default Set<AlgebraicStructure<?>> getAncestorGroups(Set<AlgebraicStructure<?>> set) {
        getSuperGroups().forEach(c -> {
            if (set.add(c)) {
                c.getAncestorGroups(set);
            }
        });
        return set;
    }

    /**
     * @return the cardinality of the complete set of this structure.
     */
    Cardinality getCardinality();

    /**
     * Whether this structure is <em>finite</em>. This is a shortcut to {@link #getCardinality()}.{@link Cardinality#isFinite() isFinite}
     */
    default boolean isFinite() {
        return getCardinality().isFinite();
    }
    default void cayleyTable(AlgebraicBinaryOperator op, Consumer<String[]> lineConsumer) {
        if (!isFinite()) {
            throw new NotStreamable("Not finite");
        }
        List<String> line = new ArrayList<>();

        line.add(op.getSymbol());
        Streamable<E> streamable = (Streamable<E>) this;
        streamable.stream().forEach(e ->
            line.add(e.toString()));
        lineConsumer.accept(line.toArray(String[]::new));
        line.clear();
        streamable.stream().forEach(e1 -> {
                line.add(e1.toString());
                streamable.stream().forEach(e2 -> {
                    line.add(op.apply(e1, e2).toString());
                });
                lineConsumer.accept(line.toArray(String[]::new));
                line.clear();
            }
            );

    }


     /**
     * @return the java class of the elements of this algebraic structure
     */
    Class<E> getElementClass();

    /**
     * @return a functional interface that can check whether two elements of this structure are equal.
     * <p>
     * Default this simply returns {@link Objects#equals(Object, Object)}.
     */
    default Equivalence<E> getEquivalence() {
        return Objects::equals;
    }

    default String getDescription() {
        return getClass().getSimpleName();
    }

    default E[][] newMatrix(int i, int j) {
        return ArrayUtils.newMatrix(getElementClass(), i, j);
    }

    @SuppressWarnings("unchecked")
    default E[] newArray(int i) {
        return (E[]) Array.newInstance(getElementClass(), i);
    }

    /**
     * Returns a random element from the structure
     */
    @Override
    default  E nextRandom(Random random) {
        throw new UnsupportedOperationException("nextRandom not implemented in " + this.getClass() + " " + this);
    }



}
