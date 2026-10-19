package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("12")); // 2 digits
        assertFalse(Phone.isValidPhone("1234567890123456")); // 16 digits
        assertFalse(Phone.isValidPhone("+1234567890123456")); // 16 digits with +
        assertFalse(Phone.isValidPhone("+")); // + only
        assertFalse(Phone.isValidPhone("9123+4567")); // + not at the start
        assertFalse(Phone.isValidPhone("++6591234567")); // two +
        assertFalse(Phone.isValidPhone("9123 4567")); // spaces are removed by the parser, not stored
        assertFalse(Phone.isValidPhone("9123-4567")); // dashes are removed by the parser, not stored

        // valid phone numbers
        assertTrue(Phone.isValidPhone("911")); // 3 digits
        assertTrue(Phone.isValidPhone("123456789012345")); // 15 digits
        assertTrue(Phone.isValidPhone("+123456789012345")); // 15 digits with +
        assertTrue(Phone.isValidPhone("91234567")); // Singapore number
        assertTrue(Phone.isValidPhone("+447911123456")); // overseas number
        assertTrue(Phone.isValidPhone("51234567")); // 8 digits but not a Singapore number
        assertTrue(Phone.isValidPhone("71234567"));
        assertTrue(Phone.isValidPhone("01234567"));
    }

    @Test
    public void isSingaporeNumber() {
        assertTrue(Phone.isSingaporeNumber("61234567")); // landline
        assertTrue(Phone.isSingaporeNumber("81234567")); // mobile starting with 8
        assertTrue(Phone.isSingaporeNumber("91234567")); // mobile starting with 9

        assertFalse(Phone.isSingaporeNumber("51234567")); // starts with 5
        assertFalse(Phone.isSingaporeNumber("71234567")); // starts with 7
        assertFalse(Phone.isSingaporeNumber("01234567")); // starts with 0
        assertFalse(Phone.isSingaporeNumber("9123456")); // 7 digits
        assertFalse(Phone.isSingaporeNumber("912345678")); // 9 digits
        assertFalse(Phone.isSingaporeNumber("+6591234567")); // +65 is removed before storing

        assertTrue(new Phone("91234567").isSingaporeNumber());
        assertFalse(new Phone("+447911123456").isSingaporeNumber());
    }

    @Test
    public void normalise() {
        assertThrows(NullPointerException.class, () -> Phone.normalise(null));

        // spaces and dashes removed
        assertEquals("91234567", Phone.normalise("9123 4567"));
        assertEquals("91234567", Phone.normalise("9123-4567"));

        // +65 removed when the rest is a Singapore number
        assertEquals("91234567", Phone.normalise("+65 9123 4567"));
        assertEquals("91234567", Phone.normalise("+6591234567"));

        // +65 kept when the rest is not a Singapore number
        assertEquals("+6512345678", Phone.normalise("+65 1234 5678"));
        assertEquals("+65", Phone.normalise("+65"));

        // overseas numbers keep their country code
        assertEquals("+447911123456", Phone.normalise("+44 7911-123456"));

        // 65 without + is not a country code
        assertEquals("6591234567", Phone.normalise("65 9123 4567"));
    }

    @Test
    public void equals() {
        Phone phone = new Phone("91234567");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("91234567")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("91234568")));
    }
}
