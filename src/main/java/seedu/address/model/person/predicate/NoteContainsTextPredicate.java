package seedu.address.model.person.predicate;

import seedu.address.model.person.Person;

/**
 * Tests that at least one of a {@code Person}'s notes contains the given text,
 * ignoring case and extra spaces.
 */
public class NoteContainsTextPredicate extends ContainsTextPredicate {

    /**
     * Creates a predicate that matches students with a note containing {@code text}.
     *
     * @param text Text to search for. Must not be blank.
     */
    public NoteContainsTextPredicate(String text) {
        super(text);
    }

    @Override
    public boolean test(Person person) {
        return person.getNotes().stream()
                .anyMatch(note -> containsText(note.value));
    }
}
