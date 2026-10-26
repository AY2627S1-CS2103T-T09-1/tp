package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;
import static seedu.address.logic.commands.HelpCommand.USER_GUIDE_URL;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private static final String EXPECTED_HELP_MESSAGE = String.join(System.lineSeparator(),
            "Available commands:",
            "",
            "help - Shows this list of available commands.",
            "clear [-f] - Clears all students from TuteeBook3; -f skips confirmation.",
            "add - Adds a student; confirms same-name records and rejects the same name, "
                    + "school, and level.",
            "edit - Edits a student's details; confirms same-name records and rejects the same name, "
                    + "school, and level.",
            "delete - Removes a student of the given index. Use -f to skip confirmation.",
            "filter - Shows the students who match all the given details, such as name, school, or level.",
            "view - Shows a student's full record on a single screen.",
            "list - Shows the entire student roster.",
            "addnote - Adds a note about a student.",
            "editnote - Replaces the text of the note with the given NID of a student of the given index.",
            "deletenote - Deletes the note with the given NID from a student of the given index. "
                    + "Use -f to skip confirmation.",
            "exit - Exits TuteeBook3.",
            "",
            "For a more detailed explanation, please refer to:",
            "https://ay2627s1-cs2103t-t09-1.github.io/tp/UserGuide.html");

    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_returnsInlineHelpAndLeavesModelUnchanged() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
        assertEquals(EXPECTED_HELP_MESSAGE, SHOWING_HELP_MESSAGE);
    }

    @Test
    public void userGuideUrl_helpLink_usesDeployedUserGuide() {
        assertEquals("https://ay2627s1-cs2103t-t09-1.github.io/tp/UserGuide.html", USER_GUIDE_URL);
    }
}
