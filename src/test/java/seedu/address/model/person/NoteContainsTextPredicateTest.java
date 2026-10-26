package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NoteContainsTextPredicateTest {

    @Test
    public void equals() {
        NoteContainsTextPredicate firstPredicate = new NoteContainsTextPredicate("algebra");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new NoteContainsTextPredicate("algebra")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(new NoteContainsTextPredicate("geometry")));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NoteContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new NoteContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("alge").test(studentWithNotes("Weak in algebra")));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("ALGEBRA").test(studentWithNotes("Weak in algebra")));
    }

    @Test
    public void test_extraSpacesInNote_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("weak in").test(studentWithNotes("Weak  in algebra")));
    }

    @Test
    public void test_symbols_returnsTrue() {
        assertTrue(new NoteContainsTextPredicate("36/50").test(studentWithNotes("WA2 Math: 36/50")));
        assertTrue(new NoteContainsTextPredicate("s/o").test(studentWithNotes("Call Rajesh s/o Kumar")));
        assertTrue(new NoteContainsTextPredicate("t/f").test(studentWithNotes("Revise t/f questions")));
    }

    @Test
    public void test_anyOfSeveralNotes_returnsTrue() {
        Person student = studentWithNotes("Strong in geometry", "Weak in algebra", "Bring calculator");
        assertTrue(new NoteContainsTextPredicate("algebra").test(student));
    }

    @Test
    public void test_noMatchingNote_returnsFalse() {
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("algebra");

        // notes without the text
        assertFalse(predicate.test(studentWithNotes("Weak in geometry")));

        // no notes
        assertFalse(predicate.test(studentWithNotes()));
    }

    @Test
    public void test_textOnlyInName_returnsFalse() {
        Person student = new PersonBuilder().withName("Algebra Tan").withNotes("Weak in geometry").build();
        assertFalse(new NoteContainsTextPredicate("algebra").test(student));
    }

    private static Person studentWithNotes(String... notes) {
        return new PersonBuilder().withNotes(notes).build();
    }
}
