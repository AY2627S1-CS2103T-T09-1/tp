package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s email contains the given text, ignoring case and extra spaces.
 */
public class EmailContainsTextPredicate implements Predicate<Person> {

    private final String text;

    /**
     * Creates a predicate that matches students whose email contains {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public EmailContainsTextPredicate(String text) {
        requireNonNull(text);
        checkArgument(!text.isBlank());
        this.text = text;
    }

    @Override
    public boolean test(Person person) {
        return person.getEmail()
                .map(email -> StringUtil.containsIgnoreCaseAndSpaces(email.value, text))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailContainsTextPredicate otherPredicate)) {
            return false;
        }

        return text.equals(otherPredicate.text);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("text", text).toString();
    }
}
