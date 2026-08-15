package com.bookseva.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a college name is either one of the predefined dropdown values
 * or, if it is a free-text "Other" entry, meets minimum quality rules:
 * - not blank
 * - at least 3 characters
 * - must contain at least one letter
 * - must not be purely numeric
 * - must not be purely symbolic (no letters)
 * - allowed characters: letters, digits, spaces, . , - & ' ( )
 */
@Documented
@Constraint(validatedBy = CollegeNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCollegeName {

    String message() default "College name is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
