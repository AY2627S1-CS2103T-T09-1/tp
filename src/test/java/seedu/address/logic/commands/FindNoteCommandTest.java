package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NoteContainsTextPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindNoteCommand}.
 */
public class FindNoteCommandTest {

    private static final Person CHLOE = new PersonBuilder().withName("Chloe Tan")
            .withNotes("Weak in algebra", "Strong in geometry", "Algebra quiz on Friday").build();
    private static final Person MARCUS = new PersonBuilder().withName("Marcus Lim")
            .withNotes("Revise trigonometry", "Practise algebra").build();
    private static final Person AISHA = new PersonBuilder().withName("Aisha Rahman")
            .withNotes("Review comprehension").build();

    private Model model = new ModelManager(getAddressBookWithNotes(), new UserPrefs());
    private Model expectedModel = new ModelManager(getAddressBookWithNotes(), new UserPrefs());

    @Test
    public void execute_someStudentsMatch_showsStudentsAndNoteNumbers() {
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("algebra");
        String expectedMessage = String.join(System.lineSeparator(),
                "2 student(s) with notes containing \"algebra\":",
                "1. Chloe Tan: notes 1, 3",
                "2. Marcus Lim: note 2");
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FindNoteCommand(predicate), model, CommandResult.showList(expectedMessage),
                expectedModel);
        assertEquals(List.of(CHLOE, MARCUS), model.getFilteredPersonList());
    }

    @Test
    public void execute_noStudentsMatch_showsNoStudentsMessage() {
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("calculus");
        String expectedMessage = "No student has a note containing \"calculus\". "
                + "Check the spelling, or type list to see all students.";
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FindNoteCommand(predicate), model, CommandResult.showList(expectedMessage),
                expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_afterOtherFilter_searchesAllStudents() {
        model.updateFilteredPersonList(AISHA::equals);
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("algebra");

        new FindNoteCommand(predicate).execute(model);

        assertEquals(List.of(CHLOE, MARCUS), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        FindNoteCommand firstCommand = new FindNoteCommand(new NoteContainsTextPredicate("algebra"));
        FindNoteCommand secondCommand = new FindNoteCommand(new NoteContainsTextPredicate("geometry"));

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        assertTrue(firstCommand.equals(new FindNoteCommand(new NoteContainsTextPredicate("algebra"))));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different text -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        NoteContainsTextPredicate predicate = new NoteContainsTextPredicate("algebra");
        FindNoteCommand command = new FindNoteCommand(predicate);
        String expected = FindNoteCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    private static AddressBook getAddressBookWithNotes() {
        return new AddressBookBuilder().withPerson(CHLOE).withPerson(MARCUS).withPerson(AISHA).build();
    }
}
