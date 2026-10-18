package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Formats full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String USER_GUIDE_URL =
            "https://ay2627s1-cs2103t-t09-1.github.io/tp/UserGuide.html";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions. "
            + "Additional arguments are not allowed.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = String.join(System.lineSeparator(),
            "Available commands:",
            "",
            "help - Shows this list of available commands.",
            "clear [-f] - Clears all students from TuteeBook3; -f skips confirmation.",
            "add - Adds a student with their name, school, subject, and contact details.",
            "delete - Removes a student of the given index.",
            "find - Finds students using partial name matching.",
            "view - Shows a student's full record on a single screen.",
            "list - Shows the entire student roster.",
            "addnote - Adds a note about a student.",
            "deletenote - Deletes the note with the given NID from a student of the given index.",
            "exit - Exits TuteeBook3.",
            "",
            "For a more detailed explanation, please refer to:",
            USER_GUIDE_URL);

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
