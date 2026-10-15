package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

/**
 * Represents a student's secondary school level.
 */
public enum Level {
    S1, S2, S3, S4, S5;

    public static final String MESSAGE_CONSTRAINTS = "Level must be one of S1, S2, S3, S4, S5.";

    /**
     * Returns the level matching the given value, ignoring case and surrounding spaces.
     *
     * @throws IllegalArgumentException if the value is not a valid level.
     */
    public static Level fromString(String value) {
        requireNonNull(value);
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS, e);
        }
    }
}
