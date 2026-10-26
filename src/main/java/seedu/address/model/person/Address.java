package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}.
 */
public class Address {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS =
            "Addresses must not be blank and must be at most 200 characters.";
    public static final String VALIDATION_REGEX = "[^\\s].*";
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
        checkArgument(isValidAddress(normalizedAddress), MESSAGE_CONSTRAINTS);
        value = normalizedAddress;
    }

    /**
     * Returns true if the given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        String normalizedAddress = normalizeAddress(test);
        return normalizedAddress.length() <= MAX_LENGTH && normalizedAddress.matches(VALIDATION_REGEX);
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
