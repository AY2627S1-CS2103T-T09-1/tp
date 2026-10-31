package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new {@code EditNoteCommand} object.
 * Everything after the note text prefix is the note text, so {@code nid/} must come before it.
 */
public class EditNoteCommandParser implements Parser<EditNoteCommand> {

    public static final String MESSAGE_NID_AFTER_TEXT = "Put " + PREFIX_NID + "NID before " + PREFIX_NOTE
            + "TEXT. Everything after " + PREFIX_NOTE + " is read as the note text.";

    /**
     * Parses the given {@code args} and returns an {@code EditNoteCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public EditNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenizeWithTrailingText(args, PREFIX_NOTE, PREFIX_NID);
        if (argMultimap.getValue(PREFIX_NID).isEmpty() && hasNid(args)) {
            throw new ParseException(MESSAGE_NID_AFTER_TEXT);
        }

        boolean isMissingArgument = argMultimap.getPreamble().isEmpty()
                || argMultimap.getValue(PREFIX_NID).isEmpty()
                || argMultimap.getValue(PREFIX_NOTE).isEmpty();
        if (isMissingArgument) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditNoteCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NID);
        Index nid = ParserUtil.parseNid(argMultimap.getValue(PREFIX_NID).get());
        Note note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        return new EditNoteCommand(index, nid, note);
    }

    /**
     * Returns true if {@code args} contains {@code nid/} anywhere, including inside the note text.
     */
    private static boolean hasNid(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_NID).getValue(PREFIX_NID).isPresent();
    }
}
