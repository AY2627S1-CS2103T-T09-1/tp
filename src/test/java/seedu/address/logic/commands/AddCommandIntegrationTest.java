package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.PersonUtil.createAddCommand;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_assignsNextStudentId() {
        // The typical address book holds student IDs 1 to 7
        Person expectedPerson = new PersonBuilder().withStudentId(8).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(expectedPerson);

        assertCommandSuccess(createAddCommand(expectedPerson), model,
                "Added student ID 8: " + Messages.format(expectedPerson), expectedModel);
        assertEquals(9, model.getNextStudentId());
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(createAddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
        assertEquals(8, model.getNextStudentId());
    }

    @Test
    public void execute_noStudentIdsLeft_throwsCommandException() {
        model = new ModelManager();
        model.addPerson(new PersonBuilder(ALICE).withStudentId(StudentId.MAX_VALUE).build());

        assertCommandFailure(createAddCommand(HOON), model, AddCommand.MESSAGE_STUDENT_ID_LIMIT_REACHED);
        assertEquals("Student ID limit reached (999999). No more students can be added.",
                AddCommand.MESSAGE_STUDENT_ID_LIMIT_REACHED);
        assertEquals(1, model.getAddressBook().getPersonList().size());
        assertEquals(AddressBook.NEXT_STUDENT_ID_NONE_LEFT, model.getNextStudentId());
    }

    @Test
    public void execute_addAfterDelete_doesNotReuseStudentId() throws Exception {
        createAddCommand(HOON).execute(model);
        model.deletePerson(model.getFilteredPersonList().get(model.getFilteredPersonList().size() - 1));
        model.deletePerson(ALICE);

        CommandResult commandResult = createAddCommand(IDA).execute(model);

        Person expectedPerson = new PersonBuilder(IDA).withStudentId(9).build();
        assertEquals("Added student ID 9: " + Messages.format(expectedPerson), commandResult.getFeedbackToUser());
        assertTrue(model.getAddressBook().getPersonList().contains(expectedPerson));
        assertEquals(10, model.getNextStudentId());
    }

}
