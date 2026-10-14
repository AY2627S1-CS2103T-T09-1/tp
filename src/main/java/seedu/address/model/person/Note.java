package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a note about a Person in the tutor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final String MESSAGE_BLANK = "Note text cannot be blank.";

    public final String value;

    /**
     * Constructs a {@code Note} from {@code text} without its leading and trailing whitespace.
     *
     * @param text Valid note text.
     */
    public Note(String text) {
        requireNonNull(text);
        checkArgument(isValidNote(text), MESSAGE_BLANK);
        value = text.strip();
    }

    /**
     * Returns true if {@code test} contains at least one character that is not whitespace.
     */
    public static boolean isValidNote(String test) {
        return !test.isBlank();
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
