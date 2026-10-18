package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers must be Singapore numbers: 8 digits starting with 6, 8 or 9, such as 91234567. "
            + "Spaces, dashes and +65 are allowed.";
    /*
     * A Singapore landline (6) or mobile (8, 9) number, stored without spaces, dashes or country code.
     */
    public static final String VALIDATION_REGEX = "[689]\\d{7}";
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
     * Returns {@code phone} with spaces and dashes removed, then a leading {@code +65} removed.
     * The result may still be invalid; check it with {@link #isValidPhone(String)}.
     */
    public static String normalise(String phone) {
        requireNonNull(phone);
        String compact = phone.replaceAll("[ -]", "");
        return compact.startsWith(COUNTRY_CODE) ? compact.substring(COUNTRY_CODE.length()) : compact;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
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
