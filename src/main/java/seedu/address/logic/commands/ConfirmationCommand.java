package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that may require confirmation before it changes data.
 */
public interface ConfirmationCommand {
    /**
     * Executes this command.
     *
     * @throws CommandException if the requested operation cannot be completed.
     */
    CommandResult execute(Model model) throws CommandException;

    /**
     * Returns whether this command should skip its confirmation prompt.
     */
    boolean isForced();

    /**
     * Returns the confirmation message, or {@code null} if confirmation is unnecessary.
     *
     * @throws CommandException if the requested operation is invalid.
     */
    String getConfirmationMessage(Model model) throws CommandException;
}
