package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ListCommand} object.
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Returns a {@code ListCommand} if no additional arguments are present.
     *
     * @throws ParseException if additional arguments are present.
     */
    public ListCommand parse(String args) throws ParseException {
        if (!args.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
        return new ListCommand();
    }
}
