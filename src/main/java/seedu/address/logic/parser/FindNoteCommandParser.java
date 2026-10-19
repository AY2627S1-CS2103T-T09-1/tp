package seedu.address.logic.parser;

import seedu.address.logic.commands.FindNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;
import seedu.address.model.person.NoteContainsTextPredicate;

/**
 * Parses input arguments and creates a new {@code FindNoteCommand} object.
 */
public class FindNoteCommandParser implements Parser<FindNoteCommand> {

    public static final String MESSAGE_BLANK_TEXT = "The text to search notes for cannot be blank. Example: "
            + FindNoteCommand.COMMAND_WORD + " algebra";
    public static final String MESSAGE_TEXT_TOO_LONG = "The text to search notes for must be at most "
            + Note.MAX_LENGTH + " characters.";

    /**
     * Parses the given {@code args} and returns a {@code FindNoteCommand}.
     *
     * @throws ParseException if the text is blank or longer than a note can be.
     */
    public FindNoteCommand parse(String args) throws ParseException {
        String text = args.strip().replaceAll("\\s+", " ");
        if (text.isEmpty()) {
            throw new ParseException(MESSAGE_BLANK_TEXT);
        }
        if (text.codePointCount(0, text.length()) > Note.MAX_LENGTH) {
            throw new ParseException(MESSAGE_TEXT_TOO_LONG);
        }
        return new FindNoteCommand(new NoteContainsTextPredicate(text));
    }
}
