package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a student's guardian in the address book.
 * Guarantees: immutable; name and phone are present and not null.
 */
public class Guardian {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Guardian name: " + Name.MESSAGE_CONSTRAINTS;
    public static final String MESSAGE_PHONE_CONSTRAINTS = "Guardian phone: " + Phone.MESSAGE_CONSTRAINTS;

    private final Name name;
    private final Phone phone;

    /**
     * Constructs a {@code Guardian}.
     *
     * @param name The guardian's name.
     * @param phone The guardian's phone number.
     */
    public Guardian(Name name, Phone phone) {
        requireAllNonNull(name, phone);
        this.name = name;
        this.phone = phone;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Guardian otherGuardian)) {
            return false;
        }

        return name.equals(otherGuardian.name)
                && phone.equals(otherGuardian.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }

}
