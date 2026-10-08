package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a note to the person identified using their displayed index.
 */
public class AddNoteCommand extends Command {

    public static final String COMMAND_WORD = "addnote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a note to the person at INDEX.\n"
            + "Parameters: INDEX (must be a positive integer) c/TEXT\n"
            + "Example: " + COMMAND_WORD + " 1 c/Weak in algebra";

    public static final String MESSAGE_SUCCESS = "Added note to %1$s: %2$s";

    private final Index targetIndex;
    private final String note;

    /**
     * Creates an {@code AddNoteCommand} to add {@code note} to the person at {@code targetIndex}.
     */
    public AddNoteCommand(Index targetIndex, String note) {
        this.targetIndex = targetIndex;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(targetIndex.getZeroBased());
        List<String> notes = new ArrayList<>(person.getNotes());
        notes.add(note);
        Person editedPerson = new Person(person.getName(), person.getPhone(), person.getEmail(),
                person.getAddress(), person.getTags(), notes);

        model.setPerson(person, editedPerson);
        model.updateFilteredPersonList(editedPerson::equals);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson.getName(), note));
    }
}
