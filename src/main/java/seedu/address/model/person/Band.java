package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents the band at which a student takes a subject.
 */
public enum Band {
    G1, G2, G3;

    public static final String MESSAGE_CONSTRAINTS = "Band must be one of G1, G2, G3.";

    /**
     * Returns the band matching the given value, ignoring case and surrounding spaces.
     */
    public static Band fromString(String value) {
        requireNonNull(value);
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS, e);
        }
    }
}
