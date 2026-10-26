package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void isValidAddress_lengthBoundary_accepts200AndRejects201() {
        String maximumLengthAddress = "A".repeat(Address.MAX_LENGTH);
        assertTrue(Address.isValidAddress(maximumLengthAddress));
        assertEquals(Address.MAX_LENGTH, new Address(maximumLengthAddress).value.length());
        assertFalse(Address.isValidAddress("A".repeat(Address.MAX_LENGTH + 1)));
    }

    @Test
    public void constructor_overMaximumLength_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Address.MESSAGE_CONSTRAINTS, () ->
                new Address("A".repeat(Address.MAX_LENGTH + 1)));
    }

    @Test
    public void constructor_repeatedOrdinarySpaces_collapsesSpaces() {
        assertEquals("123 Example Road", new Address("123  Example   Road").value);
    }
}
