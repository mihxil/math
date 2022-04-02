package org.meeuw.math.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Marks an argument as that it needs to be a prime.
 */
@Target({METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = PrimeValidator.class)
@Documented
public @interface Prime {

    String message() default "{org.meeuw.math.notAPrime}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
