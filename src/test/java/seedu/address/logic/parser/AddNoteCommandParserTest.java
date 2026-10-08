package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddNoteCommand;

public class AddNoteCommandParserTest {

    private final AddNoteCommandParser parser = new AddNoteCommandParser();

    @Test
    public void parse_validArgs_returnsAddNoteCommand() {
        AddNoteCommand expectedCommand = new AddNoteCommand(INDEX_FIRST_PERSON, "Weak in algebra");

        assertParseSuccess(parser, "1 c/Weak in algebra", expectedCommand);
        assertParseSuccess(parser, "  1   c/  Weak in algebra  ", expectedCommand);
    }

    @Test
    public void parse_missingIndexOrPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddNoteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " c/hi", expectedMessage);
        assertParseFailure(parser, " 1 hi", expectedMessage);
    }

    @Test
    public void parse_blankNote_throwsParseException() {
        assertParseFailure(parser, " 1 c/   ", AddNoteCommandParser.MESSAGE_BLANK_NOTE);
    }

    @Test
    public void parse_repeatedPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 c/a c/b", Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NOTE));
    }
}
