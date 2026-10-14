package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Note(null));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Note.MESSAGE_BLANK, () -> new Note(""));
        assertThrows(IllegalArgumentException.class, Note.MESSAGE_BLANK, () -> new Note("   "));
        assertThrows(IllegalArgumentException.class, Note.MESSAGE_BLANK, () -> new Note("\t"));
    }

    @Test
    public void constructor_paddedText_storesTrimmedText() {
        assertEquals("Weak in algebra", new Note("  Weak in algebra  ").value);
    }

    @Test
    public void isValidNote_validAndInvalidValues_returnsExpectedResult() {
        // blank text -> returns false
        assertFalse(Note.isValidNote(""));
        assertFalse(Note.isValidNote(" "));
        assertFalse(Note.isValidNote("\t"));

        // one character -> returns true
        assertTrue(Note.isValidNote("a"));

        // digits, punctuation, and slashes -> returns true
        assertTrue(Note.isValidNote("WA2 Math: 36/50"));
        assertTrue(Note.isValidNote("Rajesh s/o Kumar"));
        assertTrue(Note.isValidNote("-+.'#@!?()"));

        // leading and trailing whitespace -> returns true
        assertTrue(Note.isValidNote("  Weak in algebra  "));
    }

    @Test
    public void equals_sameAndDifferentText_returnsExpectedResult() {
        Note note = new Note("Weak in algebra");

        // same text -> returns true
        assertTrue(note.equals(new Note("Weak in algebra")));

        // same text with padding -> returns true
        Note paddedNote = new Note("  Weak in algebra ");
        assertTrue(note.equals(paddedNote));
        assertEquals(note.hashCode(), paddedNote.hashCode());

        // same object -> returns true
        assertTrue(note.equals(note));

        // null -> returns false
        assertFalse(note.equals(null));

        // different type -> returns false
        assertFalse(note.equals("Weak in algebra"));

        // different text -> returns false
        assertFalse(note.equals(new Note("Strong in algebra")));
    }

    @Test
    public void toString_validNote_returnsText() {
        assertEquals("WA2 Math: 36/50", new Note("WA2 Math: 36/50").toString());
    }
}
