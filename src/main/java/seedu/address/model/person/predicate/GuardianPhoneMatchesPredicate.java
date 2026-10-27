package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Tests that a {@code Person}'s guardian has exactly the given phone number.
 */
public class GuardianPhoneMatchesPredicate implements Predicate<Person> {

    private final Phone phone;

    /**
     * Creates a predicate that matches students whose guardian's phone is {@code phone}.
     */
    public GuardianPhoneMatchesPredicate(Phone phone) {
        requireNonNull(phone);
        this.phone = phone;
    }

    @Override
    public boolean test(Person person) {
        return person.getGuardian().map(Guardian::getPhone).map(phone::equals).orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GuardianPhoneMatchesPredicate otherPredicate)) {
            return false;
        }

        return phone.equals(otherPredicate.phone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("phone", phone).toString();
    }
}
