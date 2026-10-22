package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class GuardianNameContainsTextPredicateTest {

    @Test
    public void equals() {
        GuardianNameContainsTextPredicate firstPredicate = new GuardianNameContainsTextPredicate("grace");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new GuardianNameContainsTextPredicate("grace")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(new GuardianNameContainsTextPredicate("amy")));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new GuardianNameContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new GuardianNameContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new GuardianNameContainsTextPredicate("gra").test(studentWithGuardian("Grace Lim")));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new GuardianNameContainsTextPredicate("GRACE lim").test(studentWithGuardian("Grace Lim")));
    }

    @Test
    public void test_extraSpaces_returnsTrue() {
        GuardianNameContainsTextPredicate predicate = new GuardianNameContainsTextPredicate("grace   lim");
        assertTrue(predicate.test(studentWithGuardian("Grace Lim")));
    }

    @Test
    public void test_noGuardian_returnsFalse() {
        Person student = new PersonBuilder().withName("Grace Tan").withoutGuardian().build();
        assertFalse(new GuardianNameContainsTextPredicate("grace").test(student));
    }

    @Test
    public void test_textOnlyInStudentName_returnsFalse() {
        Person student = new PersonBuilder().withName("Marcus Lim")
                .withGuardian("Grace Lim", "98765432").build();
        assertFalse(new GuardianNameContainsTextPredicate("marcus").test(student));
    }

    private static Person studentWithGuardian(String guardianName) {
        return new PersonBuilder().withGuardian(guardianName, "98765432").build();
    }
}
