package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path OLD_FORMAT_TYPICAL_PERSONS_FILE =
            TEST_DATA_FOLDER.resolve("oldFormatTypicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path VALID_NOTES_FILE = TEST_DATA_FOLDER.resolve("validNotesAddressBook.json");
    private static final Path MISSING_NOTES_FILE = TEST_DATA_FOLDER.resolve("missingNotesAddressBook.json");
    private static final Path NULL_NOTES_FILE = TEST_DATA_FOLDER.resolve("nullNotesAddressBook.json");
    private static final Path NULL_NOTE_ENTRY_FILE =
            TEST_DATA_FOLDER.resolve("nullNoteEntryAddressBook.json");
    private static final Path BLANK_NOTE_FILE = TEST_DATA_FOLDER.resolve("blankNoteAddressBook.json");
    private static final Path TOO_LONG_NOTE_FILE = TEST_DATA_FOLDER.resolve("tooLongNoteAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_sameNameDifferentSchoolAndLevel_success() throws Exception {
        AddressBook source = new AddressBook();
        Person sameNameOtherSchool = new PersonBuilder(ALICE).withSchool("Victoria School").withLevel("S2").build();
        source.addPerson(ALICE);
        source.addPerson(sameNameOtherSchool);

        JsonSerializableAddressBook jsonAddressBook = new JsonSerializableAddressBook(source);

        assertEquals(List.of(ALICE, sameNameOtherSchool), jsonAddressBook.toModelType().getPersonList());
    }

    @Test
    public void toModelType_oldFormatFile_preservesAddresses() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(OLD_FORMAT_TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        List<Person> personsWithoutAddress = addressBookFromFile.getPersonList().stream()
                .map(person -> new PersonBuilder(person).withoutAddress().build())
                .toList();
        List<String> addresses = addressBookFromFile.getPersonList().stream()
                .map(person -> person.getAddress().map(Object::toString).orElse(null))
                .toList();
        assertEquals(TypicalPersons.getTypicalAddressBook().getPersonList(), personsWithoutAddress);
        assertEquals(List.of("123, Jurong West Ave 6, #08-111", "311, Clementi Ave 2, #02-25", "wall street",
                "10th street", "michegan ave", "little tokyo", "4th street"), addresses);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_validNotesFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(VALID_NOTES_FILE,
                JsonSerializableAddressBook.class).get();
        Person expectedPerson = new PersonBuilder(ALICE).withNotes("Weak in algebra", "WA2 Math: 36/50",
                "Rajesh s/o Kumar's mother called", "Weak in algebra", "Bring past-year papers").build();
        assertEquals(List.of(expectedPerson), dataFromFile.toModelType().getPersonList());
    }

    @Test
    public void toModelType_missingNotesFile_loadsEmptyNotes() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(MISSING_NOTES_FILE,
                JsonSerializableAddressBook.class).get();
        assertEquals(List.of(ALICE), dataFromFile.toModelType().getPersonList());
    }

    @Test
    public void toModelType_nullNotesFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(NULL_NOTES_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.NULL_NOTES_MESSAGE,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullNoteEntryFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(NULL_NOTE_ENTRY_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.NULL_NOTE_MESSAGE,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_blankNoteFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(BLANK_NOTE_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, Note.MESSAGE_BLANK, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_tooLongNoteFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TOO_LONG_NOTE_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, Note.MESSAGE_TOO_LONG, dataFromFile::toModelType);
    }

}
