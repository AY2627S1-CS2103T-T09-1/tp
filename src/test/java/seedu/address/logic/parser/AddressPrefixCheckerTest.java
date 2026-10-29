package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHOOL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

public class AddressPrefixCheckerTest {

    @Test
    public void verifyNoJoinedPrefix_invalidFieldValueAfterPrefix_allowsAddress() throws ParseException {
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2p/not-a-phone", PREFIX_PHONE);
    }

    @Test
    public void verifyNoJoinedPrefix_validFieldValuesAfterEachPrefix_reportsMissingSpace() {
        assertJoinedPrefix(PREFIX_NAME, "Alice");
        assertJoinedPrefix(PREFIX_PHONE, "91234567");
        assertJoinedPrefix(PREFIX_SCHOOL, "Victoria School");
        assertJoinedPrefix(PREFIX_EMAIL, "alice@example.com");
        assertJoinedPrefix(PREFIX_LEVEL, "S3");
        assertJoinedPrefix(PREFIX_SUBJECT, "Mathematics");
        assertJoinedPrefix(PREFIX_BAND, "G1");
        assertJoinedPrefix(PREFIX_GUARDIAN_NAME, "Mary Tan");
        assertJoinedPrefix(PREFIX_GUARDIAN_PHONE, "81234567");
        assertJoinedPrefix(PREFIX_TAG, "paid");
        assertJoinedPrefix(PREFIX_NOTE, "algebra revision");
    }

    @Test
    public void verifyNoJoinedPrefix_invalidFieldValuesOrUnsupportedPrefix_allowsAddress() throws ParseException {
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2band/G4", PREFIX_BAND);
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2sub/Science/Physics", PREFIX_SUBJECT);
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2note/", PREFIX_NOTE);
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2unknown/value", new Prefix("unknown/"));
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/a/Room 2", PREFIX_ADDRESS);
        AddressPrefixChecker.verifyNoJoinedPrefix("a/Science", PREFIX_SCHOOL);
        AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1/2", PREFIX_PHONE);
    }

    @Test
    public void verifyNoJoinedPrefix_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> AddressPrefixChecker.verifyNoJoinedPrefix(null, PREFIX_PHONE));
        assertThrows(NullPointerException.class, () ->
                AddressPrefixChecker.verifyNoJoinedPrefix("address", (Prefix[]) null));
    }

    private static void assertJoinedPrefix(Prefix prefix, String value) {
        ParseException exception = assertThrows(ParseException.class, () ->
                AddressPrefixChecker.verifyNoJoinedPrefix("Blk 1" + prefix + value, prefix));
        assertEquals(String.format(Messages.MESSAGE_MISSING_SPACE_BEFORE_PREFIX, prefix), exception.getMessage());
    }
}
