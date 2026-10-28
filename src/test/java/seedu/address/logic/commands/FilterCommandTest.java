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
import seedu.address.model.person.Level;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.person.predicate.AddressContainsTextPredicate;
import seedu.address.model.person.predicate.EmailContainsTextPredicate;
import seedu.address.model.person.predicate.GuardianNameContainsTextPredicate;
import seedu.address.model.person.predicate.GuardianPhoneMatchesPredicate;
import seedu.address.model.person.predicate.LevelMatchesPredicate;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.NoteContainsTextPredicate;
import seedu.address.model.person.predicate.PhoneMatchesPredicate;
import seedu.address.model.person.predicate.SchoolContainsTextPredicate;
import seedu.address.model.person.predicate.StudentMatchesAllPredicate;
import seedu.address.model.person.predicate.SubjectMatchesPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private static final Person CHLOE = new PersonBuilder().withName("Chloe Tan").withPhone("91234567")
            .withEmail("chloe.tan@gmail.com").withAddress("Blk 123 Bedok North Avenue 2, #04-111")
            .withGuardian("Amy Tan", "92345678")
            .withSubjects(new Subject("Mathematics", Band.G1), new Subject("Science", Band.G2)).build();
    private static final Person MARCUS = new PersonBuilder().withName("Marcus Lim").withPhone("82345678")
            .withEmail("marcus.lim@example.com").withAddress("Blk 311 Tampines Street 33, #05-212")
            .withGuardian("Grace Lim", "98765432")
            .withSubjects(new Subject("English", Band.G2), new Subject("Mathematics", Band.G2)).build();
    private static final Person AISHA = new PersonBuilder().withName("Aisha Rahman").withSubjects(
            new Subject("Chemistry", Band.G3), new Subject("Mathematics", Band.G3)).build();
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
    public void execute_level_studentsAtLevelFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        StudentMatchesAllPredicate predicate = filterOf(new LevelMatchesPredicate(Level.S1));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(ALICE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_levelAndSchool_onlyStudentsMatchingBothFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        StudentMatchesAllPredicate predicate = filterOf(new LevelMatchesPredicate(Level.S2),
                new SchoolContainsTextPredicate("northvale"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, CommandResult.showList(expectedMessage), expectedModel);
        assertEquals(List.of(BENSON), model.getFilteredPersonList());
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
    public void execute_email_studentsWithMatchingEmailFound() {
        assertContactFilterFinds(new EmailContainsTextPredicate("gmail.com"), List.of(CHLOE));
    }

    @Test
    public void execute_email_studentsWithoutEmailNotFound() {
        // Daniel Lee has no email, so he is never matched by an email filter
        assertContactFilterFinds(new EmailContainsTextPredicate("@"), List.of(CHLOE, MARCUS));
    }

    @Test
    public void execute_address_studentsWithMatchingAddressFound() {
        assertContactFilterFinds(new AddressContainsTextPredicate("bedok north"), List.of(CHLOE));
    }

    @Test
    public void execute_address_studentsWithoutAddressNotFound() {
        // Daniel Lee has no address, so he is never matched by an address filter
        assertContactFilterFinds(new AddressContainsTextPredicate("blk"), List.of(CHLOE, MARCUS));
    }

    @Test
    public void execute_note_studentsWithMatchingNoteFound() {
        Person weakInAlgebra = new PersonBuilder().withName("Chloe Tan").withNotes("Weak in algebra").build();
        Person strongInGeometry = new PersonBuilder().withName("Marcus Lim")
                .withNotes("Strong in geometry").build();
        AddressBook addressBook = new AddressBookBuilder().withPerson(weakInAlgebra)
                .withPerson(strongInGeometry).build();
        Model noteModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedNoteModel = new ModelManager(addressBook, new UserPrefs());
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        StudentMatchesAllPredicate predicate = filterOf(new NoteContainsTextPredicate("algebra"));
        expectedNoteModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(new FilterCommand(predicate), noteModel,
                CommandResult.showList(expectedMessage), expectedNoteModel);
        assertEquals(List.of(weakInAlgebra), noteModel.getFilteredPersonList());
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

    /**
     * Runs {@code filter} on a model of students with and without phones, emails, addresses and guardians,
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
