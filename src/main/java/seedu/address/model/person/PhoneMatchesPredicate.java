package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s own phone number is exactly the given phone.
 */
public class PhoneMatchesPredicate implements Predicate<Person> {

    private final Phone phone;

    /**
     * Creates a predicate that matches students whose phone is {@code phone}.
     */
    public PhoneMatchesPredicate(Phone phone) {
        requireNonNull(phone);
        this.phone = phone;
    }

    @Override
    public boolean test(Person person) {
        return person.getPhone().map(phone::equals).orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhoneMatchesPredicate otherPredicate)) {
            return false;
        }

        return phone.equals(otherPredicate.phone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("phone", phone).toString();
    }
}
