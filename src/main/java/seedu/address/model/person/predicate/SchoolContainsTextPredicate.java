package seedu.address.model.person.predicate;

import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s school contains the given text, ignoring case and extra spaces.
 */
public class SchoolContainsTextPredicate extends ContainsTextPredicate {

    /**
     * Creates a predicate that matches schools containing {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public SchoolContainsTextPredicate(String text) {
        super(text);
    }

    @Override
    public boolean test(Person person) {
        return containsText(person.getSchool().value);
    }
}
