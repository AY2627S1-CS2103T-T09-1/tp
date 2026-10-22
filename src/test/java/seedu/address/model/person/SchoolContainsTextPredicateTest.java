package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class SchoolContainsTextPredicateTest {

    @Test
    public void equals() {
        SchoolContainsTextPredicate firstPredicate = new SchoolContainsTextPredicate("victoria");
        SchoolContainsTextPredicate secondPredicate = new SchoolContainsTextPredicate("raffles");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new SchoolContainsTextPredicate("victoria")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SchoolContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new SchoolContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new SchoolContainsTextPredicate("vic").test(studentAt("Victoria School")));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new SchoolContainsTextPredicate("VICTORIA school").test(studentAt("Victoria School")));
    }

    @Test
    public void test_symbols_returnsTrue() {
        assertTrue(new SchoolContainsTextPredicate("St. Nicholas")
                .test(studentAt("CHIJ St. Nicholas Girls’ School")));
        assertTrue(new SchoolContainsTextPredicate("Girls’")
                .test(studentAt("CHIJ St. Nicholas Girls’ School")));
        assertTrue(new SchoolContainsTextPredicate("Cedar Girls'")
                .test(studentAt("Cedar Girls' Secondary School")));
    }

    @Test
    public void test_noMatch_returnsFalse() {
        assertFalse(new SchoolContainsTextPredicate("raffles").test(studentAt("Victoria School")));
    }

    @Test
    public void test_textOnlyInName_returnsFalse() {
        Person student = new PersonBuilder().withName("Victoria Tan")
                .withSchool("Raffles Institution").build();
        assertFalse(new SchoolContainsTextPredicate("victoria").test(student));
    }

    private static Person studentAt(String school) {
        return new PersonBuilder().withSchool(school).build();
    }
}
