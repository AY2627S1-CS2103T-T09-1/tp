package seedu.address.logic;

import java.util.Optional;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmationCommand;

/**
 * Manages a pending command that requires the user to confirm or cancel it.
 */
public class ConfirmationManager {
    public static final String MESSAGE_CONFIRMATION_CANCELLATION =
            "Any other input cancels this request and runs as a command.";
    public static final String MESSAGE_CONFIRMATION_SUFFIX = System.lineSeparator()
            + "Type CONFIRM (uppercase) to proceed."
            + System.lineSeparator() + MESSAGE_CONFIRMATION_CANCELLATION;
    public static final String MESSAGE_CANCELLED_FORMAT = "The '%s' command has been cancelled.";

    private ConfirmationCommand pendingCommand;

    /**
     * Returns whether a command is waiting for confirmation.
     */
    public boolean hasPendingConfirmation() {
        return pendingCommand != null;
    }

    /**
     * Creates an amber confirmation prompt and remembers its command.
     */
    public CommandResult requestConfirmation(ConfirmationCommand command, String message) {
        pendingCommand = command;
        String instruction = command.getConfirmationInstruction();
        String suffix = instruction == null ? MESSAGE_CONFIRMATION_SUFFIX
                : System.lineSeparator() + instruction + System.lineSeparator()
                        + MESSAGE_CONFIRMATION_CANCELLATION;
        return CommandResult.warning(message + suffix);
    }

    /**
     * Clears and returns the pending command, if one exists.
     */
    public Optional<ConfirmationCommand> clearPending() {
        ConfirmationCommand confirmedCommand = pendingCommand;
        pendingCommand = null;
        return Optional.ofNullable(confirmedCommand);
    }

    /**
     * Returns a notice identifying the cancelled command.
     */
    public String getCancellationNotice(ConfirmationCommand command) {
        return String.format(MESSAGE_CANCELLED_FORMAT, command.getCommandWord());
    }
}
