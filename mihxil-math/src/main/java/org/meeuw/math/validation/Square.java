package org.meeuw.math.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = SquareValidator.class)
@Documented
public @interface Square {

    String message() default "{org.meeuw.notasquare}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
