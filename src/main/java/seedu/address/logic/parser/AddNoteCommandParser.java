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

    private static final String WHITESPACE_REGEX = "\\s+";

    /**
     * Parses the given {@code args} and returns an {@code AddNoteCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenizeWithTrailingText(args, PREFIX_NOTE);
        String preamble = argMultimap.getPreamble();
        // INDEX is one word, so more words before note/, such as nid/1, are not part of the command
        boolean hasTextAfterIndex = preamble.split(WHITESPACE_REGEX).length > 1;
        boolean isInvalidFormat = preamble.isEmpty() || hasTextAfterIndex
                || argMultimap.getValue(PREFIX_NOTE).isEmpty();
        if (isInvalidFormat) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddNoteCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(preamble, AddNoteCommand.MESSAGE_USAGE);
        Note note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        return new AddNoteCommand(index, note);
    }
}
