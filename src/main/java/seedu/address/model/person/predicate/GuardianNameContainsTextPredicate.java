package seedu.address.model.person.predicate;

import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s guardian's name contains the given text, ignoring case and extra spaces.
 */
public class GuardianNameContainsTextPredicate extends ContainsTextPredicate {

    /**
     * Creates a predicate that matches students whose guardian's name contains {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public GuardianNameContainsTextPredicate(String text) {
        super(text);
    }

    @Override
    public boolean test(Person person) {
        return person.getGuardian()
                .map(guardian -> containsText(guardian.getName().fullName))
                .orElse(false);
    }
}
