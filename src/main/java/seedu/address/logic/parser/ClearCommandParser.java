package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ClearCommand} object.
 */
public class ClearCommandParser implements Parser<ClearCommand> {

    /**
     * Returns a {@code ClearCommand} if no additional arguments are present.
     *
     * @throws ParseException if additional arguments are present.
     */
    public ClearCommand parse(String args) throws ParseException {
        if (!args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
        }
        return new ClearCommand();
    }
}
