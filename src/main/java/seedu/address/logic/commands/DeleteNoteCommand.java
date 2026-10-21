package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Deletes a note, identified by its NID, from the person identified using their displayed index.
 */
public class DeleteNoteCommand extends Command implements ConfirmationCommand {

    public static final String COMMAND_WORD = "deletenote";
    public static final String FORCE_OPTION = "-f";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the note numbered NID from the person at INDEX.\n"
            + "Parameters: [" + FORCE_OPTION + "] INDEX (must be a positive integer) "
            + "nid/NID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 nid/2 (asks for confirmation)\n"
            + COMMAND_WORD + " " + FORCE_OPTION + " 1 nid/2 (deletes immediately)";

    public static final String MESSAGE_SUCCESS = "Deleted note %1$d from %2$s: %3$s";
    public static final String MESSAGE_NO_NOTES = "%1$s has no notes to delete.";
    public static final String MESSAGE_NID_NOT_FOUND = "NID %1$d does not exist. %2$s has only %3$d %4$s.";

    private final Index targetIndex;
    private final Index targetNid;
    private final boolean isForced;

    /**
     * Creates a {@code DeleteNoteCommand} to delete note {@code targetNid} of the person at
     * {@code targetIndex}.
     */
    public DeleteNoteCommand(Index targetIndex, Index targetNid) {
        this(targetIndex, targetNid, false);
    }

    /**
     * Creates a {@code DeleteNoteCommand} that bypasses confirmation when {@code isForced} is true.
     */
    public DeleteNoteCommand(Index targetIndex, Index targetNid, boolean isForced) {
        this.targetIndex = targetIndex;
        this.targetNid = targetNid;
        this.isForced = isForced;
    }

    @Override
    public boolean isForced() {
        return isForced;
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        Person person = getTargetPerson(model);
        verifyNoteExists(person);
        return "This will delete note " + targetNid.getOneBased() + " from " + person.getName() + ". Continue?";
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person person = getTargetPerson(model);
        verifyNoteExists(person);
        List<Note> notes = new ArrayList<>(person.getNotes());
        Note deletedNote = notes.remove(targetNid.getZeroBased());
        Person editedPerson = person.copyWithNotes(notes);

        model.setPerson(person, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, targetNid.getOneBased(),
                editedPerson.getName(), deletedNote));
    }

    private Person getTargetPerson(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Throws a {@code CommandException} if {@code person} has no note numbered {@code targetNid}.
     */
    private void verifyNoteExists(Person person) throws CommandException {
        int noteCount = person.getNotes().size();
        if (noteCount == 0) {
            throw new CommandException(String.format(MESSAGE_NO_NOTES, person.getName()));
        }
        if (targetNid.getZeroBased() >= noteCount) {
            String noteWord = noteCount == 1 ? "note" : "notes";
            throw new CommandException(String.format(MESSAGE_NID_NOT_FOUND, targetNid.getOneBased(),
                    person.getName(), noteCount, noteWord));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteNoteCommand otherDeleteNoteCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteNoteCommand.targetIndex)
                && targetNid.equals(otherDeleteNoteCommand.targetNid)
                && isForced == otherDeleteNoteCommand.isForced;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetNid", targetNid)
                .add("isForced", isForced)
                .toString();
    }
}
