package seedu.address.model.person.predicate;

import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s email contains the given text, ignoring case and extra spaces.
 */
public class EmailContainsTextPredicate extends ContainsTextPredicate {

    /**
     * Creates a predicate that matches students whose email contains {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public EmailContainsTextPredicate(String text) {
        super(text);
    }

    @Override
    public boolean test(Person person) {
        return person.getEmail()
                .map(email -> containsText(email.value))
                .orElse(false);
    }
}
