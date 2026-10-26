package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_NID;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "12";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "91234567";
    private static final String VALID_NOTE = "WA2 Math: 36/50";

    private static final String WHITESPACE = " \t\r\n";

    private static final String COMMAND_USAGE = "Usage message";

    @Test
    public void validateNoArguments_emptyArguments_success() throws Exception {
        ParserUtil.validateNoArguments("", COMMAND_USAGE);
    }

    @Test
    public void validateNoArguments_nonEmptyArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_USAGE);
        assertThrows(ParseException.class, expectedMessage, () ->
                ParserUtil.validateNoArguments(" extra", COMMAND_USAGE));
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseNid_validInput_returnsIndex() throws Exception {
        assertEquals(Index.fromOneBased(1), ParserUtil.parseNid("1"));
        assertEquals(Index.fromOneBased(2), ParserUtil.parseNid("  2  "));
    }

    @Test
    public void parseNid_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_NID, () -> ParserUtil.parseNid("0"));
        assertThrows(ParseException.class, MESSAGE_INVALID_NID, () -> ParserUtil.parseNid("-1"));
        assertThrows(ParseException.class, MESSAGE_INVALID_NID, () -> ParserUtil.parseNid("abc"));
        assertThrows(ParseException.class, MESSAGE_INVALID_NID, () -> ParserUtil.parseNid(""));
        assertThrows(ParseException.class, MESSAGE_INVALID_NID, () -> ParserUtil.parseNid("2147483648"));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseAddress_repeatedOrdinarySpaces_collapsesSpaces() throws Exception {
        assertEquals(new Address("123 Example Road"), ParserUtil.parseAddress("  123  Example   Road  "));
    }

    @Test
    public void parseAddress_overMaximumLength_throwsParseException() {
        String longAddress = "A".repeat(Address.MAX_LENGTH + 1);
        assertThrows(ParseException.class, Address.MESSAGE_CONSTRAINTS, () -> ParserUtil.parseAddress(longAddress));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parsePhone_formattedSingaporeNumber_returnsCleanedPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone("+65 9123 4567"));
        assertEquals(expectedPhone, ParserUtil.parsePhone("+6591234567"));
        assertEquals(expectedPhone, ParserUtil.parsePhone("9123 4567"));
        assertEquals(expectedPhone, ParserUtil.parsePhone("9123-4567"));
        assertEquals(expectedPhone, ParserUtil.parsePhone("  91234567  "));
    }

    @Test
    public void parsePhone_notSingaporeNumber_returnsCleanedPhone() throws Exception {
        assertEquals(new Phone("+442079460958"), ParserUtil.parsePhone("+44 20 7946-0958"));
        assertEquals(new Phone("+6512345678"), ParserUtil.parsePhone("+65 1234 5678")); // +65 kept
        assertEquals(new Phone("6591234567"), ParserUtil.parsePhone("65 9123 4567")); // 65 without +
        assertEquals(new Phone("9123456"), ParserUtil.parsePhone("9123 456")); // 7 digits
        assertEquals(new Phone("911"), ParserUtil.parsePhone("911")); // 3 digits
        assertEquals(new Phone("123456789012345"), ParserUtil.parsePhone("123 4567 8901 2345")); // 15 digits
    }

    @Test
    public void parsePhone_invalidCharactersOrLength_throwsParseExceptionWithReason() {
        String digitCount = " needs 3 to 15 digits, counting the country code, but has ";
        assertThrows(ParseException.class, "Phone +" + digitCount + "0.", () -> ParserUtil.parsePhone("+"));
        assertThrows(ParseException.class, "Phone 12" + digitCount + "2.", () -> ParserUtil.parsePhone(" 12 "));
        assertThrows(ParseException.class, "Phone 1234 5678 9012 3456" + digitCount + "16.", () ->
                ParserUtil.parsePhone("1234 5678 9012 3456"));

        String allowed = ", which isn't allowed. Use only digits, spaces, hyphens (-), and a + at the start.";
        assertThrows(ParseException.class, "Phone 9123.4567 contains \".\"" + allowed, () ->
                ParserUtil.parsePhone("9123.4567"));

        assertThrows(ParseException.class, "Phone 91+234567 can only have one +, at the start.", () ->
                ParserUtil.parsePhone("91+234567"));
        assertThrows(ParseException.class, "Phone cannot be empty.", () -> ParserUtil.parsePhone("  "));
    }

    @Test
    public void parseGuardianPhone_formattedSingaporeNumber_returnsCleanedPhone() throws Exception {
        assertEquals(new Phone(VALID_PHONE), ParserUtil.parseGuardianPhone("+65 9123-4567"));
    }

    @Test
    public void parseGuardianPhone_invalidValue_throwsParseExceptionNamingGuardianPhone() {
        assertThrows(ParseException.class, "Guardian phone 9123a contains \"a\", which isn't allowed. "
                + "Use only digits, spaces, hyphens (-), and a + at the start.", () ->
                ParserUtil.parseGuardianPhone("9123a"));
    }

    @Test
    public void parseNote_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseNote(null));
    }

    @Test
    public void parseNote_blankValue_throwsParseException() {
        assertThrows(ParseException.class, Note.MESSAGE_BLANK, () -> ParserUtil.parseNote(WHITESPACE));
    }

    @Test
    public void parseNote_tooLongValue_throwsParseException() {
        String tooLongNote = "a".repeat(501);
        assertThrows(ParseException.class, Note.MESSAGE_TOO_LONG, () -> ParserUtil.parseNote(tooLongNote));
    }

    @Test
    public void parseNote_validValueWithWhitespace_returnsTrimmedNote() throws Exception {
        String noteWithWhitespace = WHITESPACE + VALID_NOTE + WHITESPACE;
        assertEquals(VALID_NOTE, ParserUtil.parseNote(noteWithWhitespace).value);
    }
}
