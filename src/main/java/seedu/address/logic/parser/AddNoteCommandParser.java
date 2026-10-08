package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.logic.commands.AddNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AddNoteCommand} object.
 */
public class AddNoteCommandParser implements Parser<AddNoteCommand> {

    public static final String MESSAGE_BLANK_NOTE = "Note text cannot be blank.";

    /**
     * Parses the given {@code args} and returns an {@code AddNoteCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTE);
        if (argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddNoteCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTE);

        String note = argMultimap.getValue(PREFIX_NOTE).get().trim();
        if (note.isEmpty()) {
            throw new ParseException(MESSAGE_BLANK_NOTE);
        }
        return new AddNoteCommand(ParserUtil.parseIndex(argMultimap.getPreamble()), note);
    }
}
