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
import seedu.address.model.person.Band;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.SchoolContainsTextPredicate;
import seedu.address.model.person.StudentMatchesAllPredicate;
import seedu.address.model.person.Subject;
import seedu.address.model.person.SubjectMatchesPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private static final Person CHLOE = new PersonBuilder().withName("Chloe Tan").withSubjects(
            new Subject("Mathematics", Band.G1), new Subject("Science", Band.G2)).build();
    private static final Person MARCUS = new PersonBuilder().withName("Marcus Lim").withSubjects(
            new Subject("English", Band.G2), new Subject("Mathematics", Band.G2)).build();
    private static final Person AISHA = new PersonBuilder().withName("Aisha Rahman").withSubjects(
            new Subject("Chemistry", Band.G3), new Subject("Mathematics", Band.G3)).build();

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
    public void execute_subject_studentsTakingSubjectFound() {
        Model subjectModel = new ModelManager(getSubjectAddressBook(), new UserPrefs());
        Model expectedSubjectModel = new ModelManager(getSubjectAddressBook(), new UserPrefs());
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        StudentMatchesAllPredicate predicate = filterOf(new SubjectMatchesPredicate("Mathematics"));
        expectedSubjectModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(new FilterCommand(predicate), subjectModel,
                CommandResult.showList(expectedMessage), expectedSubjectModel);
        assertEquals(List.of(CHLOE, MARCUS, AISHA), subjectModel.getFilteredPersonList());
    }

    @Test
    public void execute_subjectAndBand_onlyThatBandFound() {
        Model subjectModel = new ModelManager(getSubjectAddressBook(), new UserPrefs());
        Model expectedSubjectModel = new ModelManager(getSubjectAddressBook(), new UserPrefs());
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        // Chloe has a G2 subject too, but it is Science, not Mathematics
        StudentMatchesAllPredicate predicate = filterOf(new SubjectMatchesPredicate("Mathematics", Band.G2));
        expectedSubjectModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(new FilterCommand(predicate), subjectModel,
                CommandResult.showList(expectedMessage), expectedSubjectModel);
        assertEquals(List.of(MARCUS), subjectModel.getFilteredPersonList());
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

    private static AddressBook getSubjectAddressBook() {
        return new AddressBookBuilder().withPerson(CHLOE).withPerson(MARCUS).withPerson(AISHA).build();
    }

    @SafeVarargs
    private static StudentMatchesAllPredicate filterOf(Predicate<Person>... filters) {
        return new StudentMatchesAllPredicate(List.of(filters));
    }
}
