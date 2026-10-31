package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a Tag in the tutor book. A tag is one word, which may contain hyphens, and tags are compared
 * ignoring case.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final int MAX_LENGTH = 30;
    public static final String MESSAGE_CONSTRAINTS = "Tags must be one word of 1-" + MAX_LENGTH
            + " English letters (A-Z) or digits (0-9). Single hyphens may join parts of the word, "
            + "such as O-level, but spaces and other symbols are not allowed. "
            + "For longer details, use a note instead.";
    public static final String MESSAGE_DUPLICATE_TAG = "Tag \"%s\" is given more than once.";
    // letters and digits, with single hyphens between them
    public static final String VALIDATION_REGEX = "\\p{Alnum}+(-\\p{Alnum}+)*";

    public final String tagName;
    private final String comparisonValue;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
        comparisonValue = tagName.toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.codePointCount(0, test.length()) <= MAX_LENGTH && test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag otherTag)) {
            return false;
        }

        return comparisonValue.equals(otherTag.comparisonValue);
    }

    @Override
    public int hashCode() {
        return comparisonValue.hashCode();
    }

    /**
     * Formats state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
