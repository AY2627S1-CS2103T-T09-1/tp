package seedu.address.logic;

import java.util.Optional;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationCommand;

/**
 * Manages a pending command that requires the user to confirm or cancel it.
 */
public class ConfirmationManager {
    public static final String MESSAGE_CONFIRMATION_SUFFIX = " (y/n)";
    public static final String MESSAGE_CANCELLED = "Cancelled. No changes were made.";
    public static final String MESSAGE_INVALID_RESPONSE = "Please answer y or n.";

    private ConfirmationCommand pendingCommand;

    /**
     * Returns whether a command is waiting for confirmation.
     */
    public boolean hasPendingConfirmation() {
        return pendingCommand != null;
    }

    /**
     * Creates a yellow confirmation prompt and remembers its command.
     */
    public CommandResult requestConfirmation(ConfirmationCommand command, String message) {
        pendingCommand = command;
        return CommandResult.warning(message + MESSAGE_CONFIRMATION_SUFFIX);
    }

    /**
     * Confirms and returns the pending command, if one exists.
     */
    public Optional<ConfirmationCommand> confirmPending() {
        ConfirmationCommand confirmedCommand = pendingCommand;
        pendingCommand = null;
        return Optional.ofNullable(confirmedCommand);
    }

    /**
     * Cancels the pending command and returns its result.
     */
    public Optional<CommandResult> cancelPending() {
        if (pendingCommand == null) {
            return Optional.empty();
        }
        pendingCommand = null;
        return Optional.of(new CommandResult(MESSAGE_CANCELLED));
    }

    /**
     * Returns a yellow response when the user did not enter y or n.
     */
    public CommandResult invalidResponse() {
        return CommandResult.warning(MESSAGE_INVALID_RESPONSE);
    }
}
