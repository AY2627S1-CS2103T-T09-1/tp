package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    /** Describes how the main content area should respond to a successful command. */
    public enum DisplayMode {
        KEEP_CURRENT,
        SHOW_LIST,
        SHOW_STUDENT
    }

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The feedback should be displayed as a warning. */
    private final boolean isWarning;

    private final DisplayMode displayMode;

    private final Person personToDisplay;

    private final Index displayedIndex;

    private final String cancellationNotice;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, false, DisplayMode.KEEP_CURRENT, null, null, null);
    }

    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean isWarning,
            DisplayMode displayMode, Person personToDisplay, Index displayedIndex, String cancellationNotice) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.isWarning = isWarning;
        this.displayMode = requireNonNull(displayMode);
        this.personToDisplay = personToDisplay;
        this.displayedIndex = displayedIndex;
        this.cancellationNotice = cancellationNotice;
        boolean requiresStudent = displayMode == DisplayMode.SHOW_STUDENT;
        if (requiresStudent) {
            requireNonNull(personToDisplay);
            requireNonNull(displayedIndex);
        }
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public Optional<Person> getPersonToDisplay() {
        return Optional.ofNullable(personToDisplay);
    }

    public Optional<Index> getDisplayedIndex() {
        return Optional.ofNullable(displayedIndex);
    }

    public Optional<String> getCancellationNotice() {
        return Optional.ofNullable(cancellationNotice);
    }

    /**
     * Creates a result that shows the student list.
     */
    public static CommandResult showList(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, false, DisplayMode.SHOW_LIST, null, null, null);
    }

    /**
     * Creates a result that shows {@code person} in the student details panel.
     */
    public static CommandResult showStudent(String feedbackToUser, Person person, Index displayedIndex) {
        return new CommandResult(feedbackToUser, false, false, false,
                DisplayMode.SHOW_STUDENT, person, displayedIndex, null);
    }

    /**
     * Creates a result whose feedback should be displayed as a warning.
     */
    public static CommandResult warning(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, true,
                DisplayMode.KEEP_CURRENT, null, null, null);
    }

    /**
     * Returns a copy of this result with {@code warning} added on a new line,
     * displayed as a warning. The display mode is kept.
     */
    public CommandResult withWarning(String warning) {
        requireNonNull(warning);
        return new CommandResult(feedbackToUser + "\n" + warning, showHelp, exit, true,
                displayMode, personToDisplay, displayedIndex, cancellationNotice);
    }

    /**
     * Returns a copy of this result with a separate cancellation notice for display.
     */
    public CommandResult withCancellationNotice(String notice) {
        requireNonNull(notice);
        return new CommandResult(feedbackToUser, showHelp, exit, isWarning,
                displayMode, personToDisplay, displayedIndex, notice);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult otherCommandResult)) {
            return false;
        }

        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && isWarning == otherCommandResult.isWarning
                && displayMode == otherCommandResult.displayMode
                && Objects.equals(personToDisplay, otherCommandResult.personToDisplay)
                && Objects.equals(displayedIndex, otherCommandResult.displayedIndex)
                && Objects.equals(cancellationNotice, otherCommandResult.cancellationNotice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, isWarning,
                displayMode, personToDisplay, displayedIndex, cancellationNotice);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("isWarning", isWarning)
                .add("displayMode", displayMode)
                .add("personToDisplay", personToDisplay)
                .add("displayedIndex", displayedIndex)
                .add("cancellationNotice", cancellationNotice)
                .toString();
    }

}
