package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names must start with an English letter (A-Z) or digit (0-9) and use only English letters, "
                    + "digits, and spaces.";

    /*
     * Names use ASCII letters and digits so their accepted character set matches School.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z0-9][A-Za-z0-9 ]*";

    public final String fullName;
    private final String comparisonValue;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
        comparisonValue = name.trim().replaceAll(" +", " ").toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the names match after trimming and collapsing spaces and ignoring case.
     */
    public boolean hasSameNormalizedName(Name otherName) {
        return otherName != null && comparisonValue.equals(otherName.comparisonValue);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
