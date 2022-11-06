package org.meeuw.math.statistics;

import java.util.Optional;
import java.util.OptionalDouble;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * Primitive {@code double} version of {@link StatisticalNumber}.
 */
public interface StatisticalDouble<SELF extends StatisticalDouble<SELF>>
    extends
    UncertainDouble<UncertainReal>,
    StatisticalNumber<SELF, Double>, UncertainReal {

    double doubleStandardDeviation();

    default double doubleMean() {
        return optionalDoubleMean().orElseThrow(() ->  new DivisionByZeroException("No values entered, cannot calculate mean"));
    }

    OptionalDouble optionalDoubleMean();

    @Override
    default Optional<Double> getOptionalMean() {
        OptionalDouble optionalDouble = optionalDoubleMean();
        if (optionalDouble.isPresent()) {
            return Optional.of(optionalDouble.getAsDouble());
        } else {
            return Optional.empty();
        }
    }

    @Override
    default Double getStandardDeviation() {
        return doubleStandardDeviation();
    }

    @Override
    default Double getValue() {
        return doubleValue();
    }

    @Override
    default Double getMean() {
        return doubleMean();
    }

    @Override
    default double doubleUncertainty() {
        return doubleStandardDeviation();
    }

    @Override
    default Double getUncertainty(){
        return doubleUncertainty();
    }


}
