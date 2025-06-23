package org.meeuw.math.validation;

import jakarta.validation.Constraint;

import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { NotZeroValidator.class })
public  @interface NotZero {

     String message() default "{org.meeuw.math.notZero}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
