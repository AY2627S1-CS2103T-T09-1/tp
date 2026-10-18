package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

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
        if (!args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE));
        }
        return new ExitCommand();
    }
}
