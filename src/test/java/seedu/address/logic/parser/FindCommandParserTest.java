package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_validName_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(List.of("avi")));
        assertParseSuccess(parser, " n/avi", expectedFindCommand);

        // space after the prefix
        assertParseSuccess(parser, " n/ avi", expectedFindCommand);
    }

    @Test
    public void parse_nameWithExtraSpaces_collapsesSpaces() {
        FindCommand expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(List.of("Chloe Tan")));
        assertParseSuccess(parser, " n/  Chloe \t  Tan  ", expectedFindCommand);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, " avi", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_textBeforePrefix_throwsParseException() {
        assertParseFailure(parser, " avi n/li", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_blankName_throwsParseException() {
        assertParseFailure(parser, " n/", FindCommandParser.MESSAGE_BLANK_NAME);
        assertParseFailure(parser, " n/   ", FindCommandParser.MESSAGE_BLANK_NAME);
    }

    @Test
    public void parse_repeatedPrefix_throwsParseException() {
        assertParseFailure(parser, " n/avi n/li", Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

}
