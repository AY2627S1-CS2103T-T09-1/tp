package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NoteContainsTextPredicateTest {

    @Test
    public void equals() {
        NoteContainsTextPredicate firstPredicate = new NoteContainsTextPredicate("algebra");
        NoteContainsTextPredicate secondPredicate = new NoteContainsTextPredicate("geometry");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same text in a different case -> returns true
        assertTrue(firstPredicate.equals(new NoteContainsTextPredicate("ALGEBRA")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NoteContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new NoteContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("alge").test(personWithNotes("Weak in algebra")));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("ALGEBRA").test(personWithNotes("Weak in algebra")));
    }

    @Test
    public void test_extraSpacesInNote_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("weak in").test(personWithNotes("Weak  in algebra")));
    }

    @Test
    public void test_symbols_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("36/50").test(personWithNotes("WA2 Math: 36/50")));
        assertTrue(new NoteContainsTextPredicate("s/o").test(personWithNotes("Call Rajesh s/o Kumar")));
    }

    @Test
    public void test_noMatchingNote_returnsFalse() {
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("algebra");

        // notes without the text
        assertFalse(predicate.test(personWithNotes("Weak in geometry")));

        // no notes
        assertFalse(predicate.test(personWithNotes()));
    }

    @Test
    public void test_textOnlyInName_returnsFalse() {
        Person person = new PersonBuilder().withName("Algebra Tan").withNotes("Weak in geometry").build();
        assertFalse(new NoteContainsTextPredicate("algebra").test(person));
    }

    @Test
    public void getMatchingNoteNumbers_someNotesMatch_returnsNumbersInOrder() {
        Person person = personWithNotes("Weak in algebra", "Strong in geometry", "Algebra quiz on Friday");
        assertEquals(List.of(1, 3), new NoteContainsTextPredicate("algebra").getMatchingNoteNumbers(person));
    }

    private static Person personWithNotes(String... notes) {
        return new PersonBuilder().withNotes(notes).build();
    }
}
