package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents an immutable subject name and its mandatory band.
 */
public final class Subject {

    public static final String MESSAGE_CONSTRAINTS =
            "Subjects must be 1-40 characters, contain at least one English letter (A-Z), and use only "
                    + "English letters, spaces, hyphens (-), parentheses, commas, and ampersands (&).";
    public static final int MAX_LENGTH = 40;
    public static final String VALIDATION_REGEX = "(?=.*[A-Za-z])[A-Za-z (),&-]+";

    private final String name;
    private final Band band;
    private final String comparisonName;

    /**
     * Constructs a subject with a valid name and a non-null band.
     */
    public Subject(String name, Band band) {
        requireNonNull(name);
        requireNonNull(band);
        checkArgument(isValidSubject(name), MESSAGE_CONSTRAINTS);
        this.name = name;
        this.band = band;
        comparisonName = toComparisonName(name);
    }

    /**
     * Returns true if the given name satisfies the subject restrictions.
     */
    public static boolean isValidSubject(String name) {
        return name.codePointCount(0, name.length()) <= MAX_LENGTH && name.matches(VALIDATION_REGEX);
    }

    public String getName() {
        return name;
    }

    public Band getBand() {
        return band;
    }

    /**
     * Returns true if the subjects have the same normalized name, regardless of band.
     */
    public boolean hasSameName(Subject other) {
        return other != null && comparisonName.equals(other.comparisonName);
    }

    /**
     * Returns true if this subject's name is {@code name}, ignoring case and extra spaces.
     */
    public boolean hasName(String name) {
        requireNonNull(name);
        return comparisonName.equals(toComparisonName(name));
    }

    private static String toComparisonName(String name) {
        return name.trim().replaceAll(" +", " ").toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Subject otherSubject)) {
            return false;
        }
        return hasSameName(otherSubject) && band == otherSubject.band;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparisonName, band);
    }

    @Override
    public String toString() {
        return name + " (" + band + ")";
    }
}
