package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers can only contain digits, with an optional + at the start, and must have 3 to 15 digits, "
            + "counting the country code. Spaces and dashes are allowed and are removed.";
    /*
     * An optional + followed by 3 to 15 digits, stored without spaces or dashes.
     * 15 is the longest international number, including the country code.
     */
    public static final String VALIDATION_REGEX = "\\+?\\d{3,15}";
    /*
     * A Singapore landline (6) or mobile (8, 9) number, stored without spaces, dashes or country code.
     */
    private static final String SINGAPORE_REGEX = "[689]\\d{7}";
    private static final String COUNTRY_CODE = "+65";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = phone;
    }

    /**
     * Returns {@code phone} with spaces and dashes removed, then a leading {@code +65} removed
     * if the rest is a Singapore number.
     * The result may still be invalid; check it with {@link #isValidPhone(String)}.
     */
    public static String normalise(String phone) {
        requireNonNull(phone);
        String compact = phone.replaceAll("[ -]", "");
        if (!compact.startsWith(COUNTRY_CODE)) {
            return compact;
        }
        String local = compact.substring(COUNTRY_CODE.length());
        return isSingaporeNumber(local) ? local : compact;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a Singapore number: 8 digits starting with 6, 8 or 9.
     */
    public static boolean isSingaporeNumber(String test) {
        return test.matches(SINGAPORE_REGEX);
    }

    /**
     * Returns true if this phone number is a Singapore number.
     */
    public boolean isSingaporeNumber() {
        return isSingaporeNumber(value);
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
        if (!(other instanceof Phone otherPhone)) {
            return false;
        }

        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
