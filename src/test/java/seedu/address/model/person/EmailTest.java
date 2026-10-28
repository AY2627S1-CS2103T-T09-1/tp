package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("-peterjack@example.com")); // local part starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack-@example.com")); // local part ends with a hyphen
        assertFalse(Email.isValidEmail("peter..jack@example.com")); // local part has two consecutive periods
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.c")); // top level domain has less than two chars
        assertFalse(Email.isValidEmail("a".repeat(Email.MAX_LENGTH - 6) + "@ex.com")); // 255 characters

        // valid email
        assertTrue(Email.isValidEmail("PeterJack_1190@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@example.com")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@bc")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // more than one period in domain
        assertTrue(Email.isValidEmail("Chloe.Tan+math@example.com.sg")); // mixed case, '+' tag, SG domain
        assertTrue(Email.isValidEmail("a".repeat(Email.MAX_LENGTH - 7) + "@ex.com")); // exactly 254 characters
    }

    @Test
    public void getInvalidReason_validEmail_returnsEmpty() {
        assertEquals(Optional.empty(), Email.getInvalidReason("a@bc"));
        assertEquals(Optional.empty(), Email.getInvalidReason("Chloe.Tan+math@example.com.sg"));
        assertEquals(Optional.empty(), Email.getInvalidReason("a".repeat(Email.MAX_LENGTH - 7) + "@ex.com"));
    }

    @Test
    public void getInvalidReason_invalidEmail_returnsReason() {
        assertReason(Email.MESSAGE_EMPTY, "");
        assertReason(String.format(Email.MESSAGE_TOO_LONG, Email.MAX_LENGTH + 1),
                "a".repeat(Email.MAX_LENGTH - 6) + "@ex.com");
        assertReason(String.format(Email.MESSAGE_HAS_SPACE, "john doe@example.com"), "john doe@example.com");
        assertReason(String.format(Email.MESSAGE_MISSING_AT, "johnexample.com"), "johnexample.com");
        assertReason(String.format(Email.MESSAGE_MULTIPLE_AT, "a@b@c.com"), "a@b@c.com");
        assertReason(String.format(Email.MESSAGE_MISSING_LOCAL_PART, "@example.com"), "@example.com");
        assertReason(String.format(Email.MESSAGE_MISSING_DOMAIN, "john@"), "john@");
        assertReason(String.format(Email.MESSAGE_INVALID_LOCAL_PART_CHARACTER, "jo!hn@example.com", "!"),
                "jo!hn@example.com");
        assertReason(String.format(Email.MESSAGE_INVALID_LOCAL_PART_CHARACTER, "j\u00f6hn@example.com", "\u00f6"),
                "j\u00f6hn@example.com");
        assertReason(String.format(Email.MESSAGE_MISPLACED_LOCAL_PART_SYMBOL, "peter..jack@example.com"),
                "peter..jack@example.com");
        assertReason(String.format(Email.MESSAGE_INVALID_DOMAIN_CHARACTER, "john@exam_ple.com", "_"),
                "john@exam_ple.com");
        assertReason(String.format(Email.MESSAGE_INVALID_DOMAIN_LABEL, "john@-example.com"), "john@-example.com");
        assertReason(String.format(Email.MESSAGE_SHORT_LAST_DOMAIN_LABEL, "john@example.c"), "john@example.c");
    }

    @Test
    public void getInvalidReason_everyRejectedEmail_returnsReason() {
        List<String> rejectedEmails = List.of("", " ", "@example.com", "peterjackexample.com", "peterjack@",
                "peterjack@-", "peterjack@exam_ple.com", "peter jack@example.com", "peterjack@exam ple.com",
                " peterjack@example.com", "peterjack@example.com ", "peterjack@@example.com",
                "peter@jack@example.com", "-peterjack@example.com", "peterjack-@example.com",
                "peter..jack@example.com", "peterjack@example@com", "peterjack@.example.com",
                "peterjack@example.com.", "peterjack@-example.com", "peterjack@example.com-",
                "peterjack@example.c", "a".repeat(Email.MAX_LENGTH - 6) + "@ex.com");
        for (String email : rejectedEmails) {
            assertTrue(Email.getInvalidReason(email).isPresent(), email);
        }
    }

    private static void assertReason(String expectedReason, String email) {
        assertEquals(Optional.of(expectedReason), Email.getInvalidReason(email));
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email")));
    }
}
