package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddressContainsTextPredicateTest {

    private static final String ADDRESS = "Blk 123 Bedok North Avenue 2, #04-111";

    @Test
    public void equals() {
        AddressContainsTextPredicate firstPredicate = new AddressContainsTextPredicate("bedok");
        AddressContainsTextPredicate secondPredicate = new AddressContainsTextPredicate("tampines");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(new AddressContainsTextPredicate("bedok")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different text -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void constructor_blankText_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new AddressContainsTextPredicate(""));
        assertThrows(IllegalArgumentException.class, () -> new AddressContainsTextPredicate("   "));
    }

    @Test
    public void test_partOfWord_returnsTrue() {
        assertTrue(new AddressContainsTextPredicate("bed").test(studentLivingAt(ADDRESS)));
    }

    @Test
    public void test_differentCase_returnsTrue() {
        assertTrue(new AddressContainsTextPredicate("BEDOK north").test(studentLivingAt(ADDRESS)));
    }

    @Test
    public void test_symbols_returnsTrue() {
        assertTrue(new AddressContainsTextPredicate("#04-111").test(studentLivingAt(ADDRESS)));
        assertTrue(new AddressContainsTextPredicate("Avenue 2, #04").test(studentLivingAt(ADDRESS)));
    }

    @Test
    public void test_noMatch_returnsFalse() {
        assertFalse(new AddressContainsTextPredicate("tampines").test(studentLivingAt(ADDRESS)));
    }

    @Test
    public void test_noAddress_returnsFalse() {
        Person student = new PersonBuilder().withoutAddress().build();
        assertFalse(new AddressContainsTextPredicate("bedok").test(student));
    }

    private static Person studentLivingAt(String address) {
        return new PersonBuilder().withAddress(address).build();
    }
}
