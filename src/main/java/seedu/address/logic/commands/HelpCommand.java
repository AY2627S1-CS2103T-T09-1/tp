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
            "add - Adds a student.",
            "Example: add n/Amy sch/NUS High lvl/S1 sub/Math band/G1",
            "",
            "edit - Edits a student's details.",
            "Example: edit 1 lvl/S3",
            "",
            "delete - Deletes a student after confirmation.",
            "Example: delete 1",
            "",
            "view - Shows a student's full record.",
            "Example: view 1",
            "",
            "list - Shows all students.",
            "Example: list",
            "",
            "filter - Filters students by their details.",
            "Example: filter n/Tan",
            "",
            "addnote - Adds a note about a student.",
            "Example: addnote 1 note/Test",
            "",
            "editnote - Edits a student's note.",
            "Example: editnote 1 nid/1 note/Updated",
            "",
            "deletenote - Deletes a note after confirmation.",
            "Example: deletenote 1 nid/1",
            "",
            "clear - Clears all students after confirmation.",
            "Example: clear",
            "",
            "help - Shows this list of available commands.",
            "Example: help",
            "",
            "exit - Exits TuteeBook3.",
            "Example: exit",
            "",
            "For full details regarding optional inputs, select and copy this URL into your browser:",
            USER_GUIDE_URL);

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
