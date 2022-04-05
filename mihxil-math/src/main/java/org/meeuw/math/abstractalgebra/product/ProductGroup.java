package org.meeuw.math.abstractalgebra.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;

/**
 * The cartesian multiplication of two other groups.
 * @author Michiel Meeuwissen
 * @since 0.8
 */

public class ProductGroup<A extends GroupElement<A>, B extends GroupElement<B>> extends AbstractAlgebraicStructure<ProductElement<A, B>>
    implements Group<ProductElement<A, B>>, Streamable<ProductElement<A, B>> {

    private static final Map<Key, ProductGroup<?, ?>>  INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <A extends GroupElement<A>, B extends GroupElement<B>> ProductGroup<A, B> of(Group<A> groupA, Group<B> groupB) {
        return (ProductGroup<A, B>) ofGeneric(groupA, groupB);
    }

    public static ProductGroup<?, ?> ofGeneric(Group<?> groupA, Group<?> groupB) {
        return INSTANCES.computeIfAbsent(new Key(groupA, groupB), (k) -> new ProductGroup<>(groupA, groupB));
    }

    @EqualsAndHashCode
    private static class Key {
        final Group<?> groupA;
        final Group<?> groupB;

        private Key(Group<?> groupA, Group<?> groupB) {
            this.groupA = groupA;
            this.groupB = groupB;
        }
    }

    @Getter
    private final Group<A> groupA;

    @Getter
    private final Group<B> groupB;


    private final ProductElement<A, B> one;


    private ProductGroup(Group<A> groupA, Group<B> groupB) {
        this.groupA = groupA;
        this.groupB = groupB;
        one = new ProductElement<>(this, groupA.unity(), groupB.unity());
    }

    @Override
    public ProductElement<A, B> unity() {
        return one;
    }

    @Override
    public Cardinality getCardinality() {
        return groupA.getCardinality().times(groupB.getCardinality());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<ProductElement<A, B>> stream() {
        if (groupA instanceof Streamable<?> && groupB instanceof Streamable<?>) {
            return StreamUtils.cartesianStream(
                () -> ((Streamable<A>) groupA).stream(),
                () -> ((Streamable<B>) groupB).stream()).map(a -> new ProductElement<>(this, (A) a[0], (B) a[1]));

        } else {
            throw new NotStreamable();
        }
    }

    @Override
    public String toString() {
        return groupA + "⨯" + groupB;
    }
}
