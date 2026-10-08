package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a Person's school in the tutor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSchool(String)}
 */
public class School {

    public static final String MESSAGE_CONSTRAINTS =
            "School names must be 1-100 characters and contain at least one letter.";
    public static final String DEFAULT_VALUE = "Unknown";
    public static final int MAX_LENGTH = 100;

    /*
     * Allows Unicode letters, spaces, hyphens, straight and curly apostrophes, and periods.
     * Requires at least one letter so punctuation-only school names are rejected.
     */
    public static final String VALIDATION_REGEX = "(?=.*\\p{L})[\\p{L} .'’-]+";

    public final String value;
    private final String comparisonValue;

    /**
     * Constructs a {@code School}.
     *
     * @param school A valid school.
     */
    public School(String school) {
        requireNonNull(school);
        checkArgument(isValidSchool(school), MESSAGE_CONSTRAINTS);
        value = school;
        comparisonValue = school.trim().replaceAll(" +", " ").toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if a given string is a valid school.
     */
    public static boolean isValidSchool(String test) {
        return test.codePointCount(0, test.length()) <= MAX_LENGTH && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof School otherSchool)) {
            return false;
        }

        return comparisonValue.equals(otherSchool.comparisonValue);
    }

    @Override
    public int hashCode() {
        return comparisonValue.hashCode();
    }

}
