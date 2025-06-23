package org.meeuw.theories;


import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * {@inheritDoc}
 *
 * All datapoints are required to be valid according to the Jakarta Bean Validation API, too.
 *
 * @author Michiel Meeuwissen
 * @since 0.18
 */
public interface ValidObjectTheory<E> extends BasicObjectTheory<E> {

     ValidatorFactory FACTORY = Validation
            .byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    @Property
    default void validate(@ForAll(DATAPOINTS) E e) {
        assertThat(FACTORY.getValidator().validate(e)).isEmpty();
    }


}
