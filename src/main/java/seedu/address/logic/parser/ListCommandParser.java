package seedu.address.logic.parser;

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
        ParserUtil.validateNoArguments(args, ListCommand.MESSAGE_USAGE);
        return new ListCommand();
    }
}
