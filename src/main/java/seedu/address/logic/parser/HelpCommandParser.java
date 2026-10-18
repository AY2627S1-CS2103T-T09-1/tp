package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code HelpCommand} object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Returns a {@code HelpCommand} if no additional arguments are present.
     *
     * @throws ParseException if additional arguments are present.
     */
    public HelpCommand parse(String args) throws ParseException {
        if (!args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        return new HelpCommand();
    }
}
