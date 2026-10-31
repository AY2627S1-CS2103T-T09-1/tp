package seedu.address.model.person.predicate;

import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s address contains the given text, ignoring case and extra spaces.
 */
public class AddressContainsTextPredicate extends ContainsTextPredicate {

    /**
     * Creates a predicate that matches students whose address contains {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public AddressContainsTextPredicate(String text) {
        super(text);
    }

    @Override
    public boolean test(Person person) {
        return person.getAddress()
                .map(address -> containsText(address.value))
                .orElse(false);
    }
}
