package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.GuardianNameContainsTextPredicate;
import seedu.address.model.person.GuardianPhoneMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhoneMatchesPredicate;
import seedu.address.model.person.SchoolContainsTextPredicate;
import seedu.address.model.person.StudentMatchesAllPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private static final Person CHLOE = new PersonBuilder().withName("Chloe Tan").withPhone("91234567")
            .withGuardian("Amy Tan", "92345678").build();
    private static final Person MARCUS = new PersonBuilder().withName("Marcus Lim").withPhone("82345678")
            .withGuardian("Grace Lim", "98765432").build();
    private static final Person DANIEL_LEE = new PersonBuilder().withName("Daniel Lee").withoutPhone()
            .withoutGuardian().build();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        StudentMatchesAllPredicate firstPredicate =
                filterOf(new NameContainsKeywordsPredicate(List.of("first")));
        StudentMatchesAllPredicate secondPredicate =
                filterOf(new NameContainsKeywordsPredicate(List.of("second")));

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = FilterCommand.MESSAGE_NO_STUDENTS_FOUND;
        StudentMatchesAllPredicate predicate = filterOf(preparePredicate(" "));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        StudentMatchesAllPredicate predicate = filterOf(preparePredicate("Kurz Elle Kunz"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_school_studentsAtMatchingSchoolsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 4);
        StudentMatchesAllPredicate predicate = filterOf(new SchoolContainsTextPredicate("secondary"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(ALICE, DANIEL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndSchool_onlyStudentsMatchingBothFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        StudentMatchesAllPredicate predicate = filterOf(new NameContainsKeywordsPredicate(List.of("meier")),
                new SchoolContainsTextPredicate("northvale"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndSchoolMatchDifferentStudents_noPersonFound() {
        String expectedMessage = FilterCommand.MESSAGE_NO_STUDENTS_FOUND;
        StudentMatchesAllPredicate predicate = filterOf(new NameContainsKeywordsPredicate(List.of("meier")),
                new SchoolContainsTextPredicate("greenhaven"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_phone_onlyExactPhoneFound() {
        assertContactFilterFinds(new PhoneMatchesPredicate(new Phone("91234567")), List.of(CHLOE));
    }

    @Test
    public void execute_guardianName_studentsWithMatchingGuardianFound() {
        // Daniel Lee has no guardian, so he is never matched by a guardian filter
        assertContactFilterFinds(new GuardianNameContainsTextPredicate("lim"), List.of(MARCUS));
    }

    @Test
    public void execute_guardianPhone_onlyExactGuardianPhoneFound() {
        assertContactFilterFinds(new GuardianPhoneMatchesPredicate(new Phone("92345678")), List.of(CHLOE));
    }

    @Test
    public void toStringMethod() {
        StudentMatchesAllPredicate predicate =
                filterOf(new NameContainsKeywordsPredicate(List.of("keyword")));
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }

    /**
     * Runs {@code filter} on a model of students with and without phones and guardians,
     * and checks that exactly {@code expectedStudents} are shown.
     */
    private static void assertContactFilterFinds(Predicate<Person> filter, List<Person> expectedStudents) {
        AddressBook addressBook = new AddressBookBuilder().withPerson(CHLOE).withPerson(MARCUS)
                .withPerson(DANIEL_LEE).build();
        Model contactModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedContactModel = new ModelManager(addressBook, new UserPrefs());
        StudentMatchesAllPredicate predicate = filterOf(filter);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedStudents.size());
        expectedContactModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(new FilterCommand(predicate), contactModel,
                CommandResult.showList(expectedMessage), expectedContactModel);
        assertEquals(expectedStudents, contactModel.getFilteredPersonList());
    }

    @SafeVarargs
    private static StudentMatchesAllPredicate filterOf(Predicate<Person>... filters) {
        return new StudentMatchesAllPredicate(List.of(filters));
    }
}
