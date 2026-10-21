package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneMatchesPredicateTest {

    private static final Phone PHONE = new Phone("91234567");

    @Test
    public void equals() {
        PhoneMatchesPredicate firstPredicate = new PhoneMatchesPredicate(PHONE);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same phone -> returns true
        assertTrue(firstPredicate.equals(new PhoneMatchesPredicate(new Phone("91234567"))));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different phone -> returns false
        assertFalse(firstPredicate.equals(new PhoneMatchesPredicate(new Phone("98765432"))));
    }

    @Test
    public void test_samePhone_returnsTrue() {
        assertTrue(new PhoneMatchesPredicate(PHONE).test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_differentPhone_returnsFalse() {
        assertFalse(new PhoneMatchesPredicate(PHONE).test(new PersonBuilder().withPhone("98765432").build()));
    }

    @Test
    public void test_partOfPhone_returnsFalse() {
        Person student = new PersonBuilder().withPhone("91234567").build();
        assertFalse(new PhoneMatchesPredicate(new Phone("9123")).test(student));
    }

    @Test
    public void test_studentWithoutPhone_returnsFalse() {
        assertFalse(new PhoneMatchesPredicate(PHONE).test(new PersonBuilder().withoutPhone().build()));
    }

    @Test
    public void test_numberOnlyOnGuardian_returnsFalse() {
        Person student = new PersonBuilder().withPhone("98765432")
                .withGuardian("Mary Tan", "91234567").build();
        assertFalse(new PhoneMatchesPredicate(PHONE).test(student));
    }

    @Test
    public void test_overseasNumber_returnsTrue() {
        Person student = new PersonBuilder().withPhone("+60123456789").build();
        assertTrue(new PhoneMatchesPredicate(new Phone("+60123456789")).test(student));
    }

    @Test
    public void toStringMethod() {
        String expected = PhoneMatchesPredicate.class.getCanonicalName() + "{phone=91234567}";
        assertEquals(expected, new PhoneMatchesPredicate(PHONE).toString());
    }
}
