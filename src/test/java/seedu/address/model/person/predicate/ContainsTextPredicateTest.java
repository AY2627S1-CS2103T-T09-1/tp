package seedu.address.model.person.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class ContainsTextPredicateTest {

    @Test
    public void equals_differentFieldSameText_returnsFalse() {
        SchoolContainsTextPredicate schoolPredicate = new SchoolContainsTextPredicate("bedok");
        AddressContainsTextPredicate addressPredicate = new AddressContainsTextPredicate("bedok");

        assertFalse(schoolPredicate.equals(addressPredicate));
        assertFalse(addressPredicate.equals(schoolPredicate));
    }

    @Test
    public void toString_subclass_namesSubclass() {
        String expected = AddressContainsTextPredicate.class.getCanonicalName() + "{text=bedok}";
        assertEquals(expected, new AddressContainsTextPredicate("bedok").toString());
    }
}
