package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a note about a Person in the tutor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final int MAX_LENGTH = 500;
    public static final String MESSAGE_BLANK = "Note text cannot be blank.";
    public static final String MESSAGE_TOO_LONG = "Note text must be at most " + MAX_LENGTH + " characters.";

    public final String value;

    /**
     * Constructs a {@code Note} from {@code text} without its leading and trailing whitespace.
     *
     * @param text Valid note text.
     */
    public Note(String text) {
        requireNonNull(text);
        checkArgument(isValidNote(text), getConstraintMessage(text));
        value = text.strip();
    }

    /**
     * Returns true if {@code test} has 1 to {@value #MAX_LENGTH} characters,
     * ignoring leading and trailing whitespace.
     */
    public static boolean isValidNote(String test) {
        return !test.isBlank() && !isTooLong(test);
    }

    /**
     * Returns the message that explains why {@code invalidText} is not valid note text.
     * The result is only meaningful if {@code invalidText} fails {@link #isValidNote(String)}.
     */
    public static String getConstraintMessage(String invalidText) {
        return invalidText.isBlank() ? MESSAGE_BLANK : MESSAGE_TOO_LONG;
    }

    /**
     * Returns true if {@code test} has more than {@value #MAX_LENGTH} characters,
     * ignoring leading and trailing whitespace.
     * Counts code points, so an emoji counts as one character.
     */
    private static boolean isTooLong(String test) {
        String strippedTest = test.strip();
        return strippedTest.codePointCount(0, strippedTest.length()) > MAX_LENGTH;
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
        if (!(other instanceof Note otherNote)) {
            return false;
        }

        return value.equals(otherNote.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
