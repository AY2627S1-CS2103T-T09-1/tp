package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StudentIdTest {

    @Test
    public void constructor_invalidStudentId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new StudentId(0));
        assertThrows(IllegalArgumentException.class, () -> new StudentId(-1));
    }

    @Test
    public void isValidStudentId() {
        // null student ID
        assertThrows(NullPointerException.class, () -> StudentId.isValidStudentId(null));

        // invalid student IDs
        assertFalse(StudentId.isValidStudentId("")); // empty string
        assertFalse(StudentId.isValidStudentId(" ")); // spaces only
        assertFalse(StudentId.isValidStudentId("0")); // zero
        assertFalse(StudentId.isValidStudentId("-1")); // negative
        assertFalse(StudentId.isValidStudentId("+1")); // plus sign
        assertFalse(StudentId.isValidStudentId("007")); // leading zeros
        assertFalse(StudentId.isValidStudentId("1.5")); // not a whole number
        assertFalse(StudentId.isValidStudentId("abc")); // non-numeric
        assertFalse(StudentId.isValidStudentId("1 2")); // spaces within digits
        assertFalse(StudentId.isValidStudentId("99999999999999999999")); // too big for an int

        // valid student IDs
        assertTrue(StudentId.isValidStudentId("1")); // smallest
        assertTrue(StudentId.isValidStudentId("42"));
        assertTrue(StudentId.isValidStudentId("2147483647")); // largest int
    }

    @Test
    public void next() {
        assertEquals(new StudentId(2), new StudentId(1).next());
    }

    @Test
    public void equals() {
        StudentId studentId = new StudentId(3);

        // same values -> returns true
        assertTrue(studentId.equals(new StudentId(3)));

        // same object -> returns true
        assertTrue(studentId.equals(studentId));

        // null -> returns false
        assertFalse(studentId.equals(null));

        // different types -> returns false
        assertFalse(studentId.equals(3));

        // different values -> returns false
        assertFalse(studentId.equals(new StudentId(4)));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(new StudentId(3).hashCode(), new StudentId(3).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("3", new StudentId(3).toString());
    }
}
