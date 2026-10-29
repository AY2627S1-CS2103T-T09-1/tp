package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Optional;

/**
 * Represents a note about a Person in the tutor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final int MAX_LENGTH = 500;
    public static final String MESSAGE_BLANK = "Note text cannot be blank.";
    public static final String MESSAGE_TOO_LONG = "Note text must be at most " + MAX_LENGTH + " characters.";
    public static final String MESSAGE_INVALID_CHARACTER = "Note text contains \"%1$s\", which isn't "
            + "allowed. Use only English letters, digits, spaces, and keyboard symbols.";
    /** Printable ASCII: a space, English letters, digits, and the symbols on a standard keyboard. */
    private static final String ALLOWED_CHARACTERS_REGEX = "[\\x20-\\x7E]";

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
     * Returns true if {@code test} has 1 to {@value #MAX_LENGTH} characters, ignoring leading and trailing
     * whitespace, and contains only printable ASCII characters.
     */
    public static boolean isValidNote(String test) {
        return !test.isBlank() && findInvalidCharacter(test).isEmpty() && !isTooLong(test);
    }

    /**
     * Returns the message that explains why {@code invalidText} is not valid note text.
     * The result is only meaningful if {@code invalidText} fails {@link #isValidNote(String)}.
     */
    public static String getConstraintMessage(String invalidText) {
        if (invalidText.isBlank()) {
            return MESSAGE_BLANK;
        }
        return findInvalidCharacter(invalidText)
                .map(character -> String.format(MESSAGE_INVALID_CHARACTER, character))
                .orElse(MESSAGE_TOO_LONG);
    }

    /**
     * Returns the first character of {@code test} that is not printable ASCII, ignoring leading and trailing
     * whitespace. A character made of two UTF-16 chars, such as an emoji, is returned whole.
     */
    private static Optional<String> findInvalidCharacter(String test) {
        String invalidCharacters = test.strip().replaceAll(ALLOWED_CHARACTERS_REGEX, "");
        if (invalidCharacters.isEmpty()) {
            return Optional.empty();
        }
        int firstLength = Character.charCount(invalidCharacters.codePointAt(0));
        return Optional.of(invalidCharacters.substring(0, firstLength));
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
