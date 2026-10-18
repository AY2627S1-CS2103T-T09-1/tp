package seedu.address.logic.parser;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ExitCommand} object.
 */
public class ExitCommandParser implements Parser<ExitCommand> {

    /**
     * Returns an {@code ExitCommand} if no additional arguments are present.
     *
     * @throws ParseException if additional arguments are present.
     */
    public ExitCommand parse(String args) throws ParseException {
        ParserUtil.validateNoArguments(args, ExitCommand.MESSAGE_USAGE);
        return new ExitCommand();
    }
}
