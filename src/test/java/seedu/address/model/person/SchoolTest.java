package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SchoolTest {

    @Test
    public void constructor_invalidSchool_throwsException() {
        assertThrows(NullPointerException.class, () -> new School(null));
        assertThrows(IllegalArgumentException.class, School.MESSAGE_CONSTRAINTS, () -> new School(""));
    }

    @Test
    public void isValidSchool_validAndInvalidValues_returnsExpectedResult() {
        assertFalse(School.isValidSchool(""));
        assertFalse(School.isValidSchool(" "));
        assertFalse(School.isValidSchool("---"));
        assertFalse(School.isValidSchool("School123"));
        assertFalse(School.isValidSchool("School (Independent)"));
        assertFalse(School.isValidSchool("a".repeat(101)));
        assertTrue(School.isValidSchool("Unknown"));
        assertTrue(School.isValidSchool("St. Joseph's Institution"));
        assertTrue(School.isValidSchool("Anglo-Chinese School"));
        assertTrue(School.isValidSchool("a".repeat(100)));
    }

    @Test
    public void equals_caseAndSpacingVariants_returnsTrue() {
        School school = new School("Bedok Green Secondary School");
        School variant = new School(" bedok  green secondary school ");
        assertEquals(school, variant);
        assertEquals(school.hashCode(), variant.hashCode());
        assertEquals("Bedok Green Secondary School", school.toString());
        assertFalse(school.equals(new School("Victoria School")));
    }
}
