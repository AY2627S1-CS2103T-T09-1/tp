package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Formats full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String USER_GUIDE_URL =
            "https://ay2627s1-cs2103t-t09-1.github.io/tp/UserGuide.html";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = String.join(System.lineSeparator(),
            "Available commands:",
            "",
            "help",
            "  Shows this list of available commands.",
            "  Example:",
            "    - help",
            "",
            "clear",
            "  Clears all students from TuteeBook3.",
            "  Example:",
            "    - clear",
            "",
            "add",
            "  Adds a student with their personal, school, subject, and contact details.",
            "  Examples:",
            "    - add n/Marcus Lim sch/Unknown lvl/S5 sub/English band/G1",
            "    - add n/Chloe Tan sch/Bedok Green Secondary School lvl/S2 sub/Mathematics band/G1 "
                    + "sub/Science band/G1 p/91234567 gn/Amy Tan gp/92345678",
            "",
            "delete",
            "  Removes student of index 3.",
            "  Example:",
            "    - delete 3",
            "",
            "find",
            "  Finds students using partial name matching.",
            "  Examples:",
            "    - find n/chloe tan",
            "    - find n/chlo",
            "",
            "view",
            "  Shows student's full record on a single screen.",
            "  Example:",
            "    - view 3",
            "",
            "list",
            "  Shows the entire student roster.",
            "  Example:",
            "    - list",
            "",
            "addnote",
            "  Adds a note about a student.",
            "  Examples:",
            "    - addnote 3 t/Strong in Algebra, weak in Geometry.",
            "    - addnote 3 t/WA2 Math: 36/50. Improved from WA1.",
            "",
            "deletenote",
            "  Removes note of index 2 from a student of index 3.",
            "  Example:",
            "    - deletenote 3 n/2",
            "",
            "exit",
            "  Exits TuteeBook3.",
            "  Example:",
            "    - exit",
            "",
            "For a more detailed explanation, please refer to:",
            USER_GUIDE_URL);

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
