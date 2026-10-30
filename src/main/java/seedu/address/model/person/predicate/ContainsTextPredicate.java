package seedu.address.model.person.predicate;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that one field of a {@code Person} contains the given text, ignoring case and extra spaces.
 * Each subclass picks the field in {@code test}.
 */
public abstract class ContainsTextPredicate implements Predicate<Person> {

    private final String text;

    /**
     * Creates a predicate that searches for {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    protected ContainsTextPredicate(String text) {
        requireNonNull(text);
        checkArgument(!text.isBlank());
        this.text = text;
    }

    /**
     * Returns true if {@code value} contains the text, ignoring case and extra spaces.
     */
    protected boolean containsText(String value) {
        return StringUtil.containsIgnoreCaseAndSpaces(value, text);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // comparing classes keeps sch/bedok and a/bedok as different filters
        if (other == null || other.getClass() != getClass()) {
            return false;
        }

        return text.equals(((ContainsTextPredicate) other).text);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("text", text).toString();
    }
}
