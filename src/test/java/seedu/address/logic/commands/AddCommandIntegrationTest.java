package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
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
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson));
        assertCommandSuccess(new AddCommand(validPerson), model, CommandResult.showList(expectedMessage),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                String.format(AddCommand.MESSAGE_DUPLICATE_PERSON, personInList.getName(),
                        personInList.getSchool(), personInList.getLevel()));
    }

    @Test
    public void execute_sameNameDifferentSchool_addsPerson() throws Exception {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        Person sameNameOtherSchool = new PersonBuilder(personInList).withSchool("Victoria School").build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(sameNameOtherSchool);

        CommandResult result = new AddCommand(sameNameOtherSchool).execute(model);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(sameNameOtherSchool)),
                result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

}
