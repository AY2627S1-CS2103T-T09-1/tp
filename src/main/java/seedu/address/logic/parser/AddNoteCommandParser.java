package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new {@code AddNoteCommand} object.
 * Everything after the note text prefix is the note text.
 */
public class AddNoteCommandParser implements Parser<AddNoteCommand> {

    /**
     * Parses the given {@code args} and returns an {@code AddNoteCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenizeWithTrailingText(args, PREFIX_NOTE);
        if (argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddNoteCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
        Note note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        return new AddNoteCommand(index, note);
    }
}
