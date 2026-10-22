package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s guardian's name contains the given text, ignoring case and extra spaces.
 */
public class GuardianNameContainsTextPredicate implements Predicate<Person> {

    private final String text;

    /**
     * Creates a predicate that matches students whose guardian's name contains {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public GuardianNameContainsTextPredicate(String text) {
        requireNonNull(text);
        checkArgument(!text.isBlank());
        this.text = text;
    }

    @Override
    public boolean test(Person person) {
        return person.getGuardian()
                .map(guardian -> StringUtil.containsIgnoreCaseAndSpaces(guardian.getName().fullName, text))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GuardianNameContainsTextPredicate otherPredicate)) {
            return false;
        }

        return text.equals(otherPredicate.text);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("text", text).toString();
    }
}
