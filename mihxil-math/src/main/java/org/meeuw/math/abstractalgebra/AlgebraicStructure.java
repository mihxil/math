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

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import org.meeuw.math.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.operators.*;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.uncertainnumbers.Uncertain;

import static java.util.Collections.unmodifiableNavigableSet;
import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.IDENTIFY;
import static org.meeuw.math.operators.OperatorInterface.COMPARATOR;

/**
 * The base interface of all algebraic structures.
 * <p>
 * If defines what arithmetic {@link BasicAlgebraicBinaryOperator}s are possible on its elements
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
     * Returns the {@link BasicAlgebraicUnaryOperator}s that elements of this structure support.
     * @return the set of all supported unary operators in this algebraic structure
     */
    default NavigableSet<AlgebraicIntOperator> getSupportedIntOperators() {
        return Collections.emptyNavigableSet();
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
     * since 0.19
     */
    default Optional<AlgebraicBinaryOperator> getOperationBySymbol(String symbol) {
        return getSupportedOperators()
            .stream()
            .filter(op -> op.getSymbol().equals(symbol))
            .findFirst();
    }

    /**
     * since 0.19
     */
    default Optional<AlgebraicUnaryOperator> getUnaryOperationBySymbol(String symbol) {
        return getSupportedUnaryOperators()
            .stream()
            .filter(op -> op.getSymbol().equals(symbol))
            .findFirst();
    }

    /**
     * since 0.19
     */
    @SuppressWarnings("unchecked")
    default Optional<E> getConstant(String symbol) {
        try {
            Method m = getClass().getMethod(symbol);
            return Optional.of((E) m.invoke(this));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {

        }
        try {
            return Optional.of(fromString(symbol));
        } catch (NotParsable notParsable) {

        }
        return Optional.empty();
    }

    /**
     * @since 0.19
     */
    default Map<String, E> getConstants() {
        Map<String, E> map = new HashMap<>();
        for (Method method : getClass().getMethods()) {
            if (method.getParameterCount() == 0
                && method.getReturnType().isAssignableFrom(getElementClass())) {
                try {
                    Synonym synonym = method.getAnnotation(Synonym.class);
                    if (synonym == null || synonym.preferred()) {
                        Object v = method.invoke(this);
                        if (getElementClass().isInstance(v)) {
                            map.put(method.getName(), (E) v);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {

                }
            }

        }
        return map;

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
     * @return All supergroups and ancestors of these
     */
    default Set<AlgebraicStructure<?>> getAncestorGroups() {
        return getAncestorGroups(new HashSet<>());
    }

    /**
     * Recursively collects all super groups and ancestors of these in the given set.
     */
    default Set<AlgebraicStructure<?>> getAncestorGroups(Set<AlgebraicStructure<?>> set) {
        getSuperGroups().forEach(c -> {
            if (set.add(c)) {
                c.getAncestorGroups(set);
            }
        });
        return set;
    }

    /**
     * The cardinality ('the number of elements') of this structure.
     * @return the cardinality of this structure.
     */
    Cardinality getCardinality();

    /**
     * Whether this structure is <em>finite</em>. This is a shortcut to {@link #getCardinality()}.{@link Cardinality#isFinite() isFinite}
     */
    default boolean isFinite() {
        return getCardinality().isFinite();
    }

    /**
     * Produces the 'Cayley' table for a certain operator (if possible).
     * This will produce (where {@code n = getCardinality().getValue()}) {@code n +1} rows of {@code n + 1} Strings. The first row
     * will be the header, consisting of the operator's symbol followed by every element of the set.
     * The {@code n} subsequence rows will have for every element of the set, this element ({@code eₙ}and then the value for {@code eₙ * e₁₋ₙ}
     *
     * @param op The operator to produce the table for
     * @param rowConsumer A consumer for every {@code n + 1} produced tuple of {@code n + 1} strings.
     * @throws NotFiniteException if the structure is not finite.
     */
    @SuppressWarnings("unchecked")
    default void cayleyTable(AlgebraicBinaryOperator op, Consumer<String[]> rowConsumer) {
        if (!isFinite()) {
            throw new NotFiniteException("Cayley table can only be produced for finite algebraic structures, but " + this + " is " + getCardinality());
        }
        List<String> line = new ArrayList<>();

        line.add(op.getSymbol());
        Streamable<E> streamable = (Streamable<E>) this;
        streamable.stream().forEach(e ->
            line.add(e.toString()));
        rowConsumer.accept(line.toArray(new String[0]));
        line.clear();
        streamable.stream().forEach(e1 -> {
                line.add(e1.toString());
                streamable.stream().forEach(e2 -> {
                    try {
                        line.add(op.apply(e1, e2).toString());
                    } catch (OperationException oe) {
                        line.add(oe.getClass().getSimpleName() + ":" + oe.getMessage());
                    }
                });
                rowConsumer.accept(line.toArray(new String[0]));
                line.clear();
            }
            );

    }


    /**
     *  The java class of the elements of this algebraic structure
     * @return the java class of the elements of this algebraic structure
     */
    Class<E> getElementClass();

    /**
     * Returns {@link Equivalence} that can check whether two elements of this structure are equal.
     * <p>
     * Default this simply returns {@link Objects#equals(Object, Object)}.
     * @return a functional interface that can check whether two elements of this structure are equal.
     */
    default Equivalence<E> getEquivalence() {
        return Objects::equals;
    }

    /**
     * Returns a description of this algebraic structure. Defaults to {@code Optional.empty()}.
     * @return A string describing this algebraic structure.
     */
    default Optional<String> getDescription() {
        return Optional.empty();
    }

    /**
     * Instantiates a new two-dimensional matrix for elements of this algebraic structure.
     * @return a new matrix of elements of this algebraic structure with the given dimensions.
     * @param i the number of rows
     * @param j the number of columns
     */
    default E[][] newMatrix(int i, int j) {
        return ArrayUtils.newMatrix(getElementClass(), i, j);
    }

    /**
     * Instantiates a new array for elements of this algebraic structure.
     * @return a new array of elements of this algebraic structure with the given dimensions.
     * @param i the number of rows
     */
    @SuppressWarnings("unchecked")
    default E[] newArray(int i) {
        return (E[]) Array.newInstance(getElementClass(), i);
    }

    /**
     * Returns a random element from the structure
     * @return a random element from the structure
     */
    @Override
    default  E nextRandom(Random random) {
         throw new UnsupportedOperationException("nextRandom not implemented in " + this.getClass() + " " + this);
    }

    /**
     * @since 0.17
     */
    default boolean elementsAreUncertain() {
        return Uncertain.class.isAssignableFrom(getElementClass());
    }


    /**
     * since 0.19
     */
    default E fromString(String value) throws NotParsable {
        return FormatService.fromString(this, value, getElementClass());
    }

    /**
     * since 0.19
     */
    default boolean isCommutative(AlgebraicBinaryOperator op) {
        return defaultIsCommutative(op, this);
    }

    /**
     * since 0.19
     */
    static <E extends AlgebraicElement<E>, S extends AlgebraicStructure<E>> boolean defaultIsCommutative(
        AlgebraicBinaryOperator  op,
        S structure) {

        if (! structure.getSupportedOperators().contains(op)) {
            throw new NoSuchOperatorException(op + " is not one of " + structure.getSupportedOperators() + " of class " + structure.getClass());
        }
        return false;
    }

}
