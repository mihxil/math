package org.meeuw.math.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import org.meeuw.math.numbers.SizeableScalar;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Marks an argument as that it needs to be a square.
 *
 * This applies to {@link Number#longValue()}, to {@link SizeableScalar#longValue()}
 * but also, to <em>arrays</em>. For a one dimensional array the length must be a square (supposing that it actually represents a 2 dimension square matrix. For a two dimension matrix it is checked whether it is properly square, i.e all rows and columns have the same size.
 *
 */
@Target({METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = SquareValidator.class)
@Documented
public @interface Square {

    String message() default "{org.meeuw.math.notASquare}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int dimension() default -1;

    boolean invertible() default false;

}
