package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddNoteCommand;
import seedu.address.model.person.Note;

public class AddNoteCommandParserTest {

    private final AddNoteCommandParser parser = new AddNoteCommandParser();

    @Test
    public void parse_validArgs_returnsAddNoteCommand() {
        AddNoteCommand expectedCommand = new AddNoteCommand(INDEX_FIRST_PERSON, new Note("Weak in algebra"));

        assertParseSuccess(parser, "1 t/Weak in algebra", expectedCommand);
        assertParseSuccess(parser, "  1   t/  Weak in algebra  ", expectedCommand);
    }

    @Test
    public void parse_noteWithSymbols_returnsAddNoteCommand() {
        assertParseSuccess(parser, "1 t/WA2 Math: 36/50",
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note("WA2 Math: 36/50")));
        assertParseSuccess(parser, "1 t/Rajesh s/o Kumar called",
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note("Rajesh s/o Kumar called")));
        assertParseSuccess(parser, "1 t/Revise t/f questions",
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note("Revise t/f questions")));
        assertParseSuccess(parser, "1 t/Call Mrs Tan c/o school",
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note("Call Mrs Tan c/o school")));
        assertParseSuccess(parser, "1 t/see nid/2",
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note("see nid/2")));
    }

    @Test
    public void parse_maxLengthNote_returnsAddNoteCommand() {
        String maxLengthText = "a".repeat(500);
        assertParseSuccess(parser, "1 t/" + maxLengthText,
                new AddNoteCommand(INDEX_FIRST_PERSON, new Note(maxLengthText)));
    }

    @Test
    public void parse_missingIndexOrPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " t/hi", expectedMessage);
        assertParseFailure(parser, " 1 hi", expectedMessage);

        // missing space between index and prefix
        assertParseFailure(parser, " 1t/hi", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " 0 t/hi", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " -1 t/hi", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 99999999999 t/hi", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_textAfterIndex_throwsIndexError() {
        assertParseFailure(parser, " 1 abc t/hi", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexAndInvalidNote_throwsIndexError() {
        assertParseFailure(parser, " 0 t/", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " abc t/   ", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 0 t/a t/b", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 0 t/" + "a".repeat(501), MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_blankNote_throwsParseException() {
        assertParseFailure(parser, " 1 t/", Note.MESSAGE_BLANK);
        assertParseFailure(parser, " 1 t/   ", Note.MESSAGE_BLANK);
    }

    @Test
    public void parse_tooLongNote_throwsParseException() {
        assertParseFailure(parser, " 1 t/" + "a".repeat(501), Note.MESSAGE_TOO_LONG);
    }

    @Test
    public void parse_trailingTextTooLong_throwsParseException() {
        assertParseFailure(parser, " 1 t/" + "a".repeat(495) + " t/abcdef", Note.MESSAGE_TOO_LONG);
    }

    @Test
    public void parse_repeatedPrefix_treatedAsNoteText() {
        assertParseSuccess(parser, "1 t/a t/b", new AddNoteCommand(INDEX_FIRST_PERSON, new Note("a t/b")));
    }
}
