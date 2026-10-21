package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Contains helper methods shared by commands that act on one note of a person.
 */
class NoteCommandUtil {

    private static final String MESSAGE_NID_NOT_FOUND = "NID %1$d does not exist. %2$s has only %3$d %4$s.";

    private NoteCommandUtil() {}

    /**
     * Throws a {@code CommandException} if {@code person} has no note numbered {@code nid}.
     *
     * @param person Person whose notes are checked.
     * @param nid NID of the note.
     * @param noNotesMessage Error shown when {@code person} has no notes, with {@code %1$s} for their name.
     * @throws CommandException If {@code person} has no notes, or fewer notes than {@code nid}.
     */
    static void verifyNoteExists(Person person, Index nid, String noNotesMessage) throws CommandException {
        int noteCount = person.getNotes().size();
        if (noteCount == 0) {
            throw new CommandException(String.format(noNotesMessage, person.getName()));
        }
        if (nid.getZeroBased() >= noteCount) {
            String noteWord = noteCount == 1 ? "note" : "notes";
            throw new CommandException(String.format(MESSAGE_NID_NOT_FOUND, nid.getOneBased(),
                    person.getName(), noteCount, noteWord));
        }
    }

    /**
     * Replaces {@code person} with {@code editedPerson} in {@code model}, keeping the edited person at the
     * same index in the filtered list even if their notes no longer match the current search.
     *
     * @param model Model that holds {@code person}.
     * @param person Person to replace.
     * @param editedPerson Person with the changed notes.
     * @param successMessage Message to show after the change.
     * @return {@code successMessage}, followed by a line saying the edited person no longer matches the
     *         last search if that is the case.
     */
    static String setPersonKeepingIndex(Model model, Person person, Person editedPerson, String successMessage) {
        boolean isOutsideFilter = model.keepPersonInFilteredList(editedPerson);
        model.setPerson(person, editedPerson);
        if (!isOutsideFilter) {
            return successMessage;
        }
        return successMessage + "\n"
                + String.format(EditCommand.MESSAGE_NO_LONGER_MATCHES, editedPerson.getName());
    }
}
