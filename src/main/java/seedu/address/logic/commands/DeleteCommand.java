package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using its displayed index from the address book.
 */
public class DeleteCommand extends Command implements ConfirmationCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String FORCE_OPTION = "-f";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the student identified by the index number used in the displayed student list.\n"
            + "Parameters: [" + FORCE_OPTION + "] INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 (asks for confirmation)\n"
            + COMMAND_WORD + " " + FORCE_OPTION + " 1 (deletes immediately)";

    public static final String MESSAGE_DELETE_STUDENT_SUCCESS = "Deleted student: %1$s at %2$s (%3$s).";
    public static final String MESSAGE_CONFIRMATION =
            "This will delete %1$s at %2$s (%3$s) and all their notes.";
    public static final String MESSAGE_CONFIRMATION_INSTRUCTION =
            "Type CONFIRM (uppercase) to delete this student and all their notes.";

    public static final String MESSAGE_INVALID_FORMAT = "Invalid format. Usage: " + COMMAND_WORD
            + " [-f] INDEX";

    public static final String MESSAGE_INVALID_STUDENT_INDEX = "The student index provided is invalid.";

    private final Index targetIndex;
    private final boolean isForced;

    /**
     * Creates a DeleteCommand that asks for confirmation.
     */
    public DeleteCommand(Index targetIndex) {
        this(targetIndex, false);
    }

    /**
     * Creates a DeleteCommand that bypasses confirmation when {@code isForced} is true.
     */
    public DeleteCommand(Index targetIndex, boolean isForced) {
        this.targetIndex = targetIndex;
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
    public String getConfirmationInstruction() {
        return MESSAGE_CONFIRMATION_INSTRUCTION;
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        Person person = getTargetPerson(model);
        return String.format(MESSAGE_CONFIRMATION, person.getName(), person.getSchool(), person.getLevel());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToDelete = getTargetPerson(model);
        model.deletePerson(personToDelete);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        String message = String.format(MESSAGE_DELETE_STUDENT_SUCCESS, personToDelete.getName(),
                personToDelete.getSchool(), personToDelete.getLevel());
        return CommandResult.showList(message);
    }

    private Person getTargetPerson(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_INDEX);
        }
        return lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteCommand.targetIndex) && isForced == otherDeleteCommand.isForced;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("isForced", isForced)
                .toString();
    }
}
