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

    private final DisplayMode displayMode;

    private final Person personToDisplay;

    private final Index displayedIndex;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, DisplayMode.KEEP_CURRENT, null, null);
    }

    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, DisplayMode displayMode,
            Person personToDisplay, Index displayedIndex) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.displayMode = requireNonNull(displayMode);
        this.personToDisplay = personToDisplay;
        this.displayedIndex = displayedIndex;
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

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public Optional<Person> getPersonToDisplay() {
        return Optional.ofNullable(personToDisplay);
    }

    public Optional<Index> getDisplayedIndex() {
        return Optional.ofNullable(displayedIndex);
    }

    /**
     * Creates a result that shows the student list.
     */
    public static CommandResult showList(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, DisplayMode.SHOW_LIST, null, null);
    }

    /**
     * Creates a result that shows {@code person} in the student details panel.
     */
    public static CommandResult showStudent(String feedbackToUser, Person person, Index displayedIndex) {
        return new CommandResult(feedbackToUser, false, false, DisplayMode.SHOW_STUDENT, person, displayedIndex);
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
                && displayMode == otherCommandResult.displayMode
                && Objects.equals(personToDisplay, otherCommandResult.personToDisplay)
                && Objects.equals(displayedIndex, otherCommandResult.displayedIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, displayMode, personToDisplay, displayedIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("displayMode", displayMode)
                .add("personToDisplay", personToDisplay)
                .add("displayedIndex", displayedIndex)
                .toString();
    }

}
