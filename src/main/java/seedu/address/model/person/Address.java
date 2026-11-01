package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.findInvalidCharacter;

/**
 * Represents a student's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}.
 */
public class Address {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS =
            "Addresses must not be blank and must be at most 200 characters.";
    public static final String MESSAGE_INVALID_CHARACTER = "Address contains \"%1$s\", which isn't "
            + "allowed. Use only English letters, digits, spaces, and keyboard symbols.";
    public static final String VALIDATION_REGEX = "[^\\s].*";
    /** Printable ASCII, plus the curly apostrophe that school names also accept, as in notes. */
    private static final String ALLOWED_CHARACTERS_REGEX = "[\\x20-\\x7E’]";
    private static final String REPEATED_SPACES_REGEX = " {2,}";
    private static final String SINGLE_SPACE = " ";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        String normalizedAddress = normalizeAddress(address);
        checkArgument(isValidAddress(normalizedAddress), getConstraintMessage(normalizedAddress));
        value = normalizedAddress;
    }

    /**
     * Returns true if the given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        String normalizedAddress = normalizeAddress(test);
        return normalizedAddress.length() <= MAX_LENGTH && normalizedAddress.matches(VALIDATION_REGEX)
                && findInvalidCharacter(normalizedAddress, ALLOWED_CHARACTERS_REGEX).isEmpty();
    }

    /**
     * Returns the message that explains why {@code invalidAddress} is not a valid address.
     * The result is only meaningful if {@code invalidAddress} fails {@link #isValidAddress(String)}.
     */
    public static String getConstraintMessage(String invalidAddress) {
        return findInvalidCharacter(invalidAddress, ALLOWED_CHARACTERS_REGEX)
                .map(character -> String.format(MESSAGE_INVALID_CHARACTER, character))
                .orElse(MESSAGE_CONSTRAINTS);
    }

    private static String normalizeAddress(String address) {
        return address.replaceAll(REPEATED_SPACES_REGEX, SINGLE_SPACE);
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
        if (!(other instanceof Address otherAddress)) {
            return false;
        }
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
