package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true)));

        // different display mode -> returns false
        assertFalse(commandResult.equals(CommandResult.showList("feedback")));

        // same student display values -> returns true
        assertTrue(CommandResult.showStudent("feedback", ALICE, INDEX_FIRST_PERSON)
                .equals(CommandResult.showStudent("feedback", ALICE, INDEX_FIRST_PERSON)));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, true).hashCode());

        // different display mode -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), CommandResult.showList("feedback").hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", exit=" + commandResult.isExit() + ", displayMode=" + commandResult.getDisplayMode()
                + ", personToDisplay=null, displayedIndex=null}";
        assertEquals(expected, commandResult.toString());
    }

    @Test
    public void showList_validFeedback_containsListDisplayInstruction() {
        CommandResult commandResult = CommandResult.showList("feedback");

        assertEquals(CommandResult.DisplayMode.SHOW_LIST, commandResult.getDisplayMode());
        assertTrue(commandResult.getPersonToDisplay().isEmpty());
        assertTrue(commandResult.getDisplayedIndex().isEmpty());
    }

    @Test
    public void showStudent_validStudent_containsStudentDisplayInstruction() {
        CommandResult commandResult = CommandResult.showStudent("feedback", ALICE, INDEX_FIRST_PERSON);

        assertEquals(CommandResult.DisplayMode.SHOW_STUDENT, commandResult.getDisplayMode());
        assertEquals(ALICE, commandResult.getPersonToDisplay().orElseThrow());
        assertEquals(INDEX_FIRST_PERSON, commandResult.getDisplayedIndex().orElseThrow());
    }

    @Test
    public void showStudent_missingStudent_throwsNullPointerException() {
        Executable showMissingStudent = () -> CommandResult.showStudent("feedback", null, INDEX_FIRST_PERSON);

        assertThrows(NullPointerException.class, showMissingStudent);
    }

    @Test
    public void showStudent_missingIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CommandResult.showStudent("feedback", ALICE, null));
    }
}
