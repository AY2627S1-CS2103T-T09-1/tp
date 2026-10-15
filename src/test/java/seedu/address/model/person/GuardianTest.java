package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GuardianTest {

    private static final Guardian GUARDIAN = new Guardian(new Name("Mary Tan"), new Phone("91234567"));

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Guardian(null, new Phone("91234567")));
        assertThrows(NullPointerException.class, () -> new Guardian(new Name("Mary Tan"), null));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Guardian copy = new Guardian(new Name("Mary Tan"), new Phone("91234567"));
        assertTrue(GUARDIAN.equals(copy));
        assertEquals(GUARDIAN.hashCode(), copy.hashCode());

        // same object -> returns true
        assertTrue(GUARDIAN.equals(GUARDIAN));

        // null -> returns false
        assertFalse(GUARDIAN.equals(null));

        // different type -> returns false
        assertFalse(GUARDIAN.equals(5));

        // different name -> returns false
        assertFalse(GUARDIAN.equals(new Guardian(new Name("John Tan"), new Phone("91234567"))));

        // different phone -> returns false
        assertFalse(GUARDIAN.equals(new Guardian(new Name("Mary Tan"), new Phone("98765432"))));
    }

    @Test
    public void toString_nameAndPhone_showsPhoneInBrackets() {
        assertEquals("Mary Tan (91234567)", GUARDIAN.toString());
    }
}
