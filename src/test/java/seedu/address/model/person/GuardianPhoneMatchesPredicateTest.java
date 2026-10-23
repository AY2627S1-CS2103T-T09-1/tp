package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class GuardianPhoneMatchesPredicateTest {

    private static final Phone PHONE = new Phone("91234567");

    @Test
    public void equals() {
        GuardianPhoneMatchesPredicate firstPredicate = new GuardianPhoneMatchesPredicate(PHONE);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same phone -> returns true
        assertTrue(firstPredicate.equals(new GuardianPhoneMatchesPredicate(new Phone("91234567"))));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different phone -> returns false
        assertFalse(firstPredicate.equals(new GuardianPhoneMatchesPredicate(new Phone("98765432"))));
    }

    @Test
    public void test_sameGuardianPhone_returnsTrue() {
        assertTrue(new GuardianPhoneMatchesPredicate(PHONE).test(studentWithGuardianPhone("91234567")));
    }

    @Test
    public void test_differentGuardianPhone_returnsFalse() {
        assertFalse(new GuardianPhoneMatchesPredicate(PHONE).test(studentWithGuardianPhone("98765432")));
    }

    @Test
    public void test_partOfPhone_returnsFalse() {
        GuardianPhoneMatchesPredicate predicate = new GuardianPhoneMatchesPredicate(new Phone("9123"));
        assertFalse(predicate.test(studentWithGuardianPhone("91234567")));
    }

    @Test
    public void test_noGuardian_returnsFalse() {
        Person student = new PersonBuilder().withoutGuardian().build();
        assertFalse(new GuardianPhoneMatchesPredicate(PHONE).test(student));
    }

    @Test
    public void test_numberOnlyOnStudent_returnsFalse() {
        Person student = new PersonBuilder().withPhone("91234567")
                .withGuardian("Mary Tan", "98765432").build();
        assertFalse(new GuardianPhoneMatchesPredicate(PHONE).test(student));
    }

    @Test
    public void toStringMethod() {
        String expected = GuardianPhoneMatchesPredicate.class.getCanonicalName() + "{phone=91234567}";
        assertEquals(expected, new GuardianPhoneMatchesPredicate(PHONE).toString());
    }

    private static Person studentWithGuardianPhone(String guardianPhone) {
        return new PersonBuilder().withGuardian("Mary Tan", guardianPhone).build();
    }
}
