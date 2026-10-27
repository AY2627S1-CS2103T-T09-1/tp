package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_containsSpace_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Tag.MESSAGE_CONSTRAINTS, () -> new Tag("exam prep"));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName(" paid")); // leading space
        assertFalse(Tag.isValidTagName("-paid")); // starts with a hyphen
        assertFalse(Tag.isValidTagName("paid-")); // ends with a hyphen
        assertFalse(Tag.isValidTagName("exam--prep")); // two hyphens in a row
        assertFalse(Tag.isValidTagName("exam_prep")); // underscore
        assertFalse(Tag.isValidTagName("#paid")); // other symbol
        assertFalse(Tag.isValidTagName("exam prep")); // more than one word
        assertFalse(Tag.isValidTagName("paid ")); // trailing space
        assertFalse(Tag.isValidTagName("a".repeat(Tag.MAX_LENGTH + 1))); // too long

        // valid tag names
        assertTrue(Tag.isValidTagName("paid")); // letters only
        assertTrue(Tag.isValidTagName("2026")); // digits only
        assertTrue(Tag.isValidTagName("Sec4Exams")); // letters and digits
        assertTrue(Tag.isValidTagName("O-level")); // hyphen between letters
        assertTrue(Tag.isValidTagName("sec-4-exams")); // several single hyphens
        assertTrue(Tag.isValidTagName("a".repeat(Tag.MAX_LENGTH))); // exactly the maximum length
    }

    @Test
    public void equals() {
        Tag tag = new Tag("Exams");

        // same values -> returns true
        assertTrue(tag.equals(new Tag("Exams")));

        // different case -> returns true
        assertTrue(tag.equals(new Tag("exams")));
        assertTrue(tag.equals(new Tag("EXAMS")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different types -> returns false
        assertFalse(tag.equals(5.0f));

        // different values -> returns false
        assertFalse(tag.equals(new Tag("paid")));
    }

    @Test
    public void hashCode_differentCase_sameHashCode() {
        assertEquals(new Tag("Paid").hashCode(), new Tag("paid").hashCode());
    }

    @Test
    public void toString_keepsOriginalCase() {
        assertEquals("[Exams]", new Tag("Exams").toString());
    }
}
