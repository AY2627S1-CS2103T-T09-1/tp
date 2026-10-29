package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DeleteNoteCommand} object.
 */
public class DeleteNoteCommandParser implements Parser<DeleteNoteCommand> {

    /**
     * Parses the given {@code args} and returns a {@code DeleteNoteCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public DeleteNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NID);
        if (argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_NID).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE));
        }

        String[] preambleParts = argMultimap.getPreamble().split("\\s+");
        boolean isForced = preambleParts.length == 2
                && preambleParts[0].equals(DeleteNoteCommand.FORCE_OPTION);
        if (preambleParts.length > 2 || preambleParts.length == 0
                || (preambleParts.length == 2 && !isForced)) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteNoteCommand.MESSAGE_USAGE));
        }
        String indexArgument = isForced ? preambleParts[1] : preambleParts[0];
        Index index = ParserUtil.parseIndex(indexArgument, DeleteNoteCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NID);
        Index nid = ParserUtil.parseNid(argMultimap.getValue(PREFIX_NID).get());
        return new DeleteNoteCommand(index, nid, isForced);
    }
}
