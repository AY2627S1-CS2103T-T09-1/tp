package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_NID;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteNoteCommand;

public class DeleteNoteCommandParserTest {

    private final DeleteNoteCommandParser parser = new DeleteNoteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteNoteCommand() {
        DeleteNoteCommand expectedCommand = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2));

        assertParseSuccess(parser, "1 note/2", expectedCommand);
        assertParseSuccess(parser, "  1   note/  2  ", expectedCommand);
    }

    @Test
    public void parse_maxIntNid_returnsDeleteNoteCommand() {
        assertParseSuccess(parser, "1 note/2147483647",
                new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(Integer.MAX_VALUE)));
    }

    @Test
    public void parse_missingIndexOrPrefix_throwsParseException() {
        String expectedMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " note/2", expectedMessage);
        assertParseFailure(parser, " 1", expectedMessage);

        // NID given without its prefix
        assertParseFailure(parser, " 1 2", expectedMessage);

        // missing space between index and prefix
        assertParseFailure(parser, " 1note/2", expectedMessage);

        // addnote's note text prefix instead of note/
        assertParseFailure(parser, " 1 c/2", expectedMessage);

        // prefixes are case-sensitive
        assertParseFailure(parser, " 1 NOTE/2", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " 0 note/1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " -1 note/1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " abc note/1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 99999999999 note/1", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_extraTextBeforePrefix_throwsIndexError() {
        assertParseFailure(parser, " 1 abc note/2", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 1 2 note/3", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidNid_throwsParseException() {
        assertParseFailure(parser, " 1 note/0", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/-1", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/abc", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/99999999999", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/1.5", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/+1", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/2 abc", MESSAGE_INVALID_NID);
    }

    @Test
    public void parse_emptyNid_throwsParseException() {
        assertParseFailure(parser, " 1 note/", MESSAGE_INVALID_NID);
        assertParseFailure(parser, " 1 note/   ", MESSAGE_INVALID_NID);
    }

    @Test
    public void parse_invalidIndexAndInvalidNid_throwsIndexError() {
        assertParseFailure(parser, " 0 note/0", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " abc note/", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " 0 note/1 note/2", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_repeatedPrefix_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NID);

        assertParseFailure(parser, " 1 note/1 note/2", expectedMessage);
        assertParseFailure(parser, " 1 note/1 note/1", expectedMessage);

        // repeated prefix is reported before the invalid NID
        assertParseFailure(parser, " 1 note/abc note/2", expectedMessage);
    }
}
