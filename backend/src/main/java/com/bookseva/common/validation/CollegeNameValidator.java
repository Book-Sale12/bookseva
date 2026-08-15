package com.bookseva.common.validation;

import com.bookseva.common.CollegeList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates a collegeName field:
 *
 * 1. If the value exactly matches one of the predefined CollegeList entries → VALID (no further checks).
 * 2. If the value is the literal string "Other" → INVALID (frontend should replace this with the typed name).
 * 3. Otherwise (free-text path from "Other" selection):
 *    - Must not be blank
 *    - Minimum 3 characters
 *    - Must contain at least one letter (rejects purely numeric or symbol-only entries)
 *    - Must not be purely numeric
 *    - Allowed characters: letters, digits, spaces, and . , - & ' ( )
 */
public class CollegeNameValidator implements ConstraintValidator<ValidCollegeName, String> {

    // Allowed character set for free-text college names
    private static final java.util.regex.Pattern ALLOWED_CHARS =
            java.util.regex.Pattern.compile("^[a-zA-Z0-9 .,\\-&'()]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null || value.isBlank()) {
            customMessage(ctx, "College name must not be empty.");
            return false;
        }

        String trimmed = value.trim();

        // Case 1: Exact match in predefined list — always valid
        if (CollegeList.PREDEFINED.contains(trimmed)) {
            return true;
        }

        // Case 2: Raw "Other" sentinel must never reach the backend
        if ("Other".equalsIgnoreCase(trimmed)) {
            customMessage(ctx, "Please enter your college name in the text field.");
            return false;
        }

        // Case 3: Free-text entry — apply quality rules
        if (trimmed.length() < 3) {
            customMessage(ctx, "College name must be at least 3 characters.");
            return false;
        }

        // Must contain at least one letter
        if (!trimmed.chars().anyMatch(Character::isLetter)) {
            customMessage(ctx, "College name cannot be only numbers or symbols.");
            return false;
        }

        // Must not be purely numeric
        if (trimmed.matches("\\d+")) {
            customMessage(ctx, "College name cannot be only numbers or symbols.");
            return false;
        }

        // Must only contain allowed characters
        if (!ALLOWED_CHARS.matcher(trimmed).matches()) {
            customMessage(ctx, "College name contains invalid characters. Allowed: letters, digits, spaces, . , - & ' ( )");
            return false;
        }

        return true;
    }

    private void customMessage(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
