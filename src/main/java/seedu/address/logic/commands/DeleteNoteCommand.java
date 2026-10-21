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
public class DeleteNoteCommand extends Command {

    public static final String COMMAND_WORD = "deletenote";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the note numbered NID from the person at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) nid/NID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 nid/2";

    public static final String MESSAGE_SUCCESS = "Deleted note %1$d from %2$s: %3$s";
    public static final String MESSAGE_NO_NOTES = "%1$s has no notes to delete.";

    private final Index targetIndex;
    private final Index targetNid;

    /**
     * Creates a {@code DeleteNoteCommand} to delete note {@code targetNid} of the person at
     * {@code targetIndex}.
     */
    public DeleteNoteCommand(Index targetIndex, Index targetNid) {
        this.targetIndex = targetIndex;
        this.targetNid = targetNid;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(targetIndex.getZeroBased());
        NoteCommandUtil.verifyNoteExists(person, targetNid, MESSAGE_NO_NOTES);
        List<Note> notes = new ArrayList<>(person.getNotes());
        Note deletedNote = notes.remove(targetNid.getZeroBased());
        Person editedPerson = person.copyWithNotes(notes);

        String successMessage = String.format(MESSAGE_SUCCESS, targetNid.getOneBased(),
                editedPerson.getName(), deletedNote);
        return new CommandResult(
                NoteCommandUtil.setPersonKeepingIndex(model, person, editedPerson, successMessage));
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
                && targetNid.equals(otherDeleteNoteCommand.targetNid);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetNid", targetNid)
                .toString();
    }
}
