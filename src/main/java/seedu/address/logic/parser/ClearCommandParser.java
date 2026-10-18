package seedu.address.logic.parser;

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
        ParserUtil.validateNoArguments(args, ClearCommand.MESSAGE_USAGE);
        return new ClearCommand();
    }
}
