package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ClearCommand} object.
 */
public class ClearCommandParser implements Parser<ClearCommand> {

    /**
     * Returns a {@code ClearCommand} with the requested confirmation behavior.
     *
     * @throws ParseException if an unsupported argument is present.
     */
    public ClearCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new ClearCommand();
        }
        if (trimmedArgs.equals(ClearCommand.FORCE_OPTION)) {
            return new ClearCommand(true);
        }
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
    }
}
