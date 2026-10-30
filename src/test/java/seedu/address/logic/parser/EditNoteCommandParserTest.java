package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_NID;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.model.person.Note;

public class EditNoteCommandParserTest {

    private static final String NOTE_TEXT = "Improving in algebra";
    private static final Index NID_SECOND = Index.fromOneBased(2);

    private final EditNoteCommandParser parser = new EditNoteCommandParser();

    @Test
    public void parse_validArgs_returnsEditNoteCommand() {
        EditNoteCommand expectedCommand = new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(NOTE_TEXT));

        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + NOTE_TEXT, expectedCommand);
        assertParseSuccess(parser, "  1   nid/  2   " + PREFIX_NOTE + "  " + NOTE_TEXT + "  ", expectedCommand);
    }

    @Test
    public void parse_nidAfterText_throwsParseException() {
        String expectedMessage = EditNoteCommandParser.MESSAGE_NID_AFTER_TEXT;

        assertParseFailure(parser, " 1 " + PREFIX_NOTE + NOTE_TEXT + " nid/2", expectedMessage);
        assertParseFailure(parser, " " + PREFIX_NOTE + NOTE_TEXT + " nid/2", expectedMessage);

        // real nid/ left out, but the text contains nid/
        assertParseFailure(parser, " 1 " + PREFIX_NOTE + "Retest on nid/2", expectedMessage);
    }

    @Test
    public void parse_prefixesInsideText_keepsWholeText() {
        String careOfNote = "Call Mrs Tan c/o school";
        String trueFalseNote = "Revise t/f questions";
        String nidNote = "Redo nid/2 next week";
        String repeatedPrefixNote = "a " + PREFIX_NOTE + "b";

        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + careOfNote,
                new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(careOfNote)));
        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + trueFalseNote,
                new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(trueFalseNote)));
        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + nidNote,
                new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(nidNote)));
        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + repeatedPrefixNote,
                new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(repeatedPrefixNote)));
    }

    @Test
    public void parse_noteWithSymbols_returnsEditNoteCommand() {
        String symbolNote = "Rajesh s/o Kumar: WA2 36/50 (Mary-Jane's class) +5 marks. J. Tan helped";

        assertParseSuccess(parser, "1 nid/2 " + PREFIX_NOTE + symbolNote,
                new EditNoteCommand(INDEX_FIRST_PERSON, NID_SECOND, new Note(symbolNote)));
    }

    @Test
    public void parse_maxLengthNoteAndMaxIntNid_returnsEditNoteCommand() {
        String maxLengthText = "a".repeat(Note.MAX_LENGTH);

        assertParseSuccess(parser, "1 nid/2147483647 " + PREFIX_NOTE + maxLengthText,
                new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(Integer.MAX_VALUE),
                        new Note(maxLengthText)));
    }

    @Test
    public void parse_missingIndexOrPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " nid/2 " + PREFIX_NOTE + "hi", expectedMessage);
        assertParseFailure(parser, " 1 " + PREFIX_NOTE + "hi", expectedMessage);
        assertParseFailure(parser, " 1 nid/2", expectedMessage);

        // NID or text given without its prefix
        assertParseFailure(parser, " 1 2 " + PREFIX_NOTE + "hi", expectedMessage);
        assertParseFailure(parser, " 1 nid/2 hi", expectedMessage);

        // missing space before a prefix
        assertParseFailure(parser, " 1nid/2 " + PREFIX_NOTE + "hi", expectedMessage);
        assertParseFailure(parser, " 1 nid/2" + PREFIX_NOTE + "hi", expectedMessage);

        // prefixes are case-sensitive
        assertParseFailure(parser, " 1 NID/2 " + PREFIX_NOTE + "hi", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " 0 nid/1 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " -1 nid/1 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 99999999999 nid/1 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_nonIntegerIndex_throwsCommandFormatError() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " abc nid/1 " + PREFIX_NOTE + "hi", expectedMessage);

        // text after a valid index
        assertParseFailure(parser, " 1 abc nid/2 " + PREFIX_NOTE + "hi", expectedMessage);
    }

    @Test
    public void parse_invalidNid_throwsParseException() {
        assertParseFailure(parser, " 1 nid/0 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 nid/-1 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 nid/abc " + PREFIX_NOTE + "hi", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 nid/99999999999 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 nid/ " + PREFIX_NOTE + "hi", MESSAGE_INVALID_NID);
    }

    @Test
    public void parse_blankNote_throwsParseException() {
        assertParseFailure(parser, " 1 nid/2 " + PREFIX_NOTE, Note.MESSAGE_BLANK);
        assertParseFailure(parser, " 1 nid/2 " + PREFIX_NOTE + "   ", Note.MESSAGE_BLANK);
    }

    @Test
    public void parse_tooLongNote_throwsParseException() {
        assertParseFailure(parser, " 1 nid/2 " + PREFIX_NOTE + "a".repeat(Note.MAX_LENGTH + 1),
                Note.MESSAGE_TOO_LONG);
    }

    @Test
    public void parse_severalInvalidValues_reportsIndexThenNid() {
        assertParseFailure(parser, " 0 nid/0 " + PREFIX_NOTE, MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 0 nid/1 nid/2 " + PREFIX_NOTE + "hi", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 1 nid/0 " + PREFIX_NOTE, MESSAGE_INVALID_NID);
    }

    @Test
    public void parse_missingPartAndOtherInvalidValues_reportsMissingPartFirst() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " 0 nid/1", expectedMessage);
        assertParseFailure(parser, " 1 nid/abc", expectedMessage);
        assertParseFailure(parser, " 1 nid/1 nid/2", expectedMessage);
    }

    @Test
    public void parse_repeatedNid_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NID);

        assertParseFailure(parser, " 1 nid/1 nid/2 " + PREFIX_NOTE + "hi", expectedMessage);
        assertParseFailure(parser, " 1 nid/1 nid/1 " + PREFIX_NOTE + "hi", expectedMessage);

        // repeated NID is reported before the invalid NID
        assertParseFailure(parser, " 1 nid/abc nid/2 " + PREFIX_NOTE + "hi", expectedMessage);
    }
}
