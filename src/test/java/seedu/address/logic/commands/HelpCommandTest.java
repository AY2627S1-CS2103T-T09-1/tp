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
