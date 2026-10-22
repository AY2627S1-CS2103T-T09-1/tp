package seedu.address.logic.parser;

import java.math.BigInteger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final String DIGITS_REGEX = "\\d+";

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] parts = trimmedArgs.isEmpty() ? new String[0] : trimmedArgs.split("\\s+");
        boolean isForced = parts.length == 2 && parts[0].equals(DeleteCommand.FORCE_OPTION);
        String indexArgument = isForced ? parts[1] : parts.length == 1 ? parts[0] : trimmedArgs;
        if (parts.length != 1 && !isForced) {
            throw new ParseException(DeleteCommand.MESSAGE_INVALID_FORMAT);
        }
        try {
            Index index = ParserUtil.parseIndex(indexArgument);
            return new DeleteCommand(index, isForced);
        } catch (ParseException pe) {
            throw createParseException(indexArgument, pe);
        }
    }

    /**
     * Returns the error for an index that could not be parsed.
     * A positive whole number too big to store cannot match any student, so it is reported as an invalid index.
     */
    private static ParseException createParseException(String trimmedArgs, ParseException cause) {
        if (!trimmedArgs.matches(DIGITS_REGEX)) {
            return new ParseException(DeleteCommand.MESSAGE_INVALID_FORMAT, cause);
        }
        boolean isZero = new BigInteger(trimmedArgs).signum() == 0;
        if (isZero) {
            return new ParseException(DeleteCommand.MESSAGE_INVALID_FORMAT, cause);
        }
        return new ParseException(DeleteCommand.MESSAGE_INVALID_STUDENT_INDEX, cause);
    }

}
