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
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
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
