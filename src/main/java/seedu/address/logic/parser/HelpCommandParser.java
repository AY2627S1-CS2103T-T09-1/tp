package seedu.address.logic.parser;

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
        ParserUtil.validateNoArguments(args, HelpCommand.MESSAGE_USAGE);
        return new HelpCommand();
    }
}
