package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class SubjectMatchesPredicateTest {

    private static final Subject MATHEMATICS_G1 = new Subject("Mathematics", Band.G1);
    private static final Subject ENGLISH_G3 = new Subject("English", Band.G3);

    @Test
    public void equals() {
        SubjectMatchesPredicate mathematicsG1 = new SubjectMatchesPredicate("Mathematics", Band.G1);

        // same object -> returns true
        assertTrue(mathematicsG1.equals(mathematicsG1));

        // same name and band -> returns true
        assertTrue(mathematicsG1.equals(new SubjectMatchesPredicate("Mathematics", Band.G1)));

        // same name, any band -> returns true
        SubjectMatchesPredicate mathematicsAnyBand = new SubjectMatchesPredicate("Mathematics");
        assertTrue(mathematicsAnyBand.equals(new SubjectMatchesPredicate("Mathematics")));

        // different types -> returns false
        assertFalse(mathematicsG1.equals(1));

        // null -> returns false
        assertFalse(mathematicsG1.equals(null));

        // different name -> returns false
        assertFalse(mathematicsG1.equals(new SubjectMatchesPredicate("English", Band.G1)));

        // different band -> returns false
        assertFalse(mathematicsG1.equals(new SubjectMatchesPredicate("Mathematics", Band.G2)));

        // any band vs a set band -> returns false
        assertFalse(mathematicsG1.equals(new SubjectMatchesPredicate("Mathematics")));
    }

    @Test
    public void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SubjectMatchesPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new SubjectMatchesPredicate("  ", Band.G1));
    }

    @Test
    public void constructor_nullBand_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SubjectMatchesPredicate("Mathematics", null));
    }

    @Test
    public void test_subjectAnyBand_returnsTrue() {
        Person student = studentTaking(MATHEMATICS_G1, ENGLISH_G3);
        assertTrue(new SubjectMatchesPredicate("Mathematics").test(student));
    }

    @Test
    public void test_differentCaseAndSpaces_returnsTrue() {
        Person student = studentTaking(MATHEMATICS_G1);
        assertTrue(new SubjectMatchesPredicate("MATHEMATICS").test(student));
        assertTrue(new SubjectMatchesPredicate(" mathematics ").test(student));
    }

    @Test
    public void test_partialSubjectName_returnsFalse() {
        assertFalse(new SubjectMatchesPredicate("math").test(studentTaking(MATHEMATICS_G1)));
    }

    @Test
    public void test_subjectAtSameBand_returnsTrue() {
        assertTrue(new SubjectMatchesPredicate("Mathematics", Band.G1).test(studentTaking(MATHEMATICS_G1)));
    }

    @Test
    public void test_subjectAtDifferentBand_returnsFalse() {
        assertFalse(new SubjectMatchesPredicate("Mathematics", Band.G2).test(studentTaking(MATHEMATICS_G1)));
    }

    @Test
    public void test_bandOnOtherSubject_returnsFalse() {
        // the student has a G3 subject, but it is English, not Mathematics
        Person student = studentTaking(MATHEMATICS_G1, ENGLISH_G3);
        assertFalse(new SubjectMatchesPredicate("Mathematics", Band.G3).test(student));
    }

    @Test
    public void test_hyphenatedSubject_returnsTrue() {
        Person student = studentTaking(new Subject("E-Math", Band.G2));
        assertTrue(new SubjectMatchesPredicate("e-math").test(student));
    }

    @Test
    public void test_studentWithoutSubject_returnsFalse() {
        assertFalse(new SubjectMatchesPredicate("Mathematics").test(studentTaking(ENGLISH_G3)));
    }

    @Test
    public void toStringMethod() {
        String prefix = SubjectMatchesPredicate.class.getCanonicalName();
        assertEquals(prefix + "{subjectName=Mathematics, band=G1}",
                new SubjectMatchesPredicate("Mathematics", Band.G1).toString());
        assertEquals(prefix + "{subjectName=Mathematics, band=any}",
                new SubjectMatchesPredicate("Mathematics").toString());
    }

    private static Person studentTaking(Subject... subjects) {
        return new PersonBuilder().withSubjects(subjects).build();
    }
}
