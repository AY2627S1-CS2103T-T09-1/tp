package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s school contains the given text, ignoring case and extra spaces.
 */
public class SchoolContainsTextPredicate implements Predicate<Person> {

    private final String text;

    /**
     * Creates a predicate that matches schools containing {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public SchoolContainsTextPredicate(String text) {
        requireNonNull(text);
        checkArgument(!text.isBlank());
        this.text = text;
    }

    @Override
    public boolean test(Person person) {
        return StringUtil.containsIgnoreCaseAndSpaces(person.getSchool().value, text);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SchoolContainsTextPredicate otherPredicate)) {
            return false;
        }

        return text.equals(otherPredicate.text);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("text", text).toString();
    }
}
