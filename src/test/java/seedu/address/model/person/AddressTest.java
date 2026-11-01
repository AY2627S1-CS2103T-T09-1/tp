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

    @Test
    public void constructor_nonAsciiAddress_throwsIllegalArgumentException() {
        String expectedMessage = String.format(Address.MESSAGE_INVALID_CHARACTER, "É");
        assertThrows(IllegalArgumentException.class, expectedMessage, () -> new Address("Rue de l'Église 5"));
    }

    @Test
    public void isValidAddress_printableAsciiAndOtherCharacters_returnsExpectedResult() {
        // every printable ASCII character after the space -> returns true
        StringBuilder printableAscii = new StringBuilder();
        for (char c = '!'; c <= '~'; c++) {
            printableAscii.append(c);
        }
        assertTrue(Address.isValidAddress(printableAscii.toString()));

        // curly apostrophe, as in school names -> returns true
        assertTrue(Address.isValidAddress("12 St. Michael’s Road"));

        // other curly quotes -> returns false
        assertFalse(Address.isValidAddress("‘Blk 5’ Bedok"));

        // accented letter -> returns false
        assertFalse(Address.isValidAddress("Rue de l'Église 5"));

        // non-Latin script -> returns false
        assertFalse(Address.isValidAddress("北京路 5"));

        // emoji -> returns false
        assertFalse(Address.isValidAddress("Blk 5 😀"));
    }

    @Test
    public void getConstraintMessage_severalInvalidCharacters_namesFirst() {
        assertEquals(String.format(Address.MESSAGE_INVALID_CHARACTER, "é"),
                Address.getConstraintMessage("Café 北京路"));
    }

    @Test
    public void getConstraintMessage_emoji_namesWholeEmoji() {
        String emoji = "😀";
        assertEquals(String.format(Address.MESSAGE_INVALID_CHARACTER, emoji),
                Address.getConstraintMessage("Blk 5 " + emoji));
    }

    @Test
    public void getConstraintMessage_tooLongWithInvalidCharacter_namesCharacter() {
        assertEquals(String.format(Address.MESSAGE_INVALID_CHARACTER, "É"),
                Address.getConstraintMessage("É" + "a".repeat(Address.MAX_LENGTH)));
    }

    @Test
    public void getConstraintMessage_blankOrTooLong_returnsConstraints() {
        assertEquals(Address.MESSAGE_CONSTRAINTS, Address.getConstraintMessage(""));
        assertEquals(Address.MESSAGE_CONSTRAINTS, Address.getConstraintMessage("a".repeat(Address.MAX_LENGTH + 1)));
    }
}
