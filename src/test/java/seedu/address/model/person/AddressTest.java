package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void isValidAddress_lengthBoundary_accepts200AndRejects201() {
        String maximumAddress = "A".repeat(Address.MAX_LENGTH);
        assertTrue(Address.isValidAddress(maximumAddress));
        assertEquals(maximumAddress, new Address(maximumAddress).value);
        assertFalse(Address.isValidAddress(maximumAddress + "A"));
    }

    @Test
    public void constructor_repeatedOrdinarySpaces_collapsesSpaces() {
        assertEquals("12 Example Road", new Address("12  Example   Road").value);
    }

    @Test
    public void constructor_blank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Address.MESSAGE_CONSTRAINTS, () -> new Address("  "));
    }
}
