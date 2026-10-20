package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that may require confirmation before it changes data.
 */
public interface ConfirmationCommand {
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

    /**
     * Executes this command after confirmation or when it is forced.
     *
     * @throws CommandException if the requested operation cannot be completed.
     */
    CommandResult executeConfirmed(Model model) throws CommandException;
}
