package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class EmailContainsTextPredicateTest {

    private static final String EMAIL = "chloe.tan+math@gmail.com";

    @Test
    public void equals() {
        EmailContainsTextPredicate firstPredicate = new EmailContainsTextPredicate("gmail");
        EmailContainsTextPredicate secondPredicate = new EmailContainsTextPredicate("yahoo");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new EmailContainsTextPredicate("gmail")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EmailContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new EmailContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new EmailContainsTextPredicate("chlo").test(studentWithEmail(EMAIL)));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new EmailContainsTextPredicate("GMAIL.com").test(studentWithEmail(EMAIL)));
    }

    @Test
    public void test_symbols_returnsTrue() {
        assertTrue(new EmailContainsTextPredicate("@gmail.com").test(studentWithEmail(EMAIL)));
        assertTrue(new EmailContainsTextPredicate("tan+math").test(studentWithEmail(EMAIL)));
    }

    @Test
    public void test_noMatch_returnsFalse() {
        assertFalse(new EmailContainsTextPredicate("yahoo").test(studentWithEmail(EMAIL)));
    }

    @Test
    public void test_noEmail_returnsFalse() {
        Person student = new PersonBuilder().withoutEmail().build();
        assertFalse(new EmailContainsTextPredicate("gmail").test(student));
    }

    private static Person studentWithEmail(String email) {
        return new PersonBuilder().withEmail(email).build();
    }
}
