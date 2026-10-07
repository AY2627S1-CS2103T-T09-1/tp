package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path DUPLICATE_STUDENT_ID_FILE =
            TEST_DATA_FOLDER.resolve("duplicateStudentIdAddressBook.json");
    private static final Path STUDENT_ID_TOO_LARGE_FILE =
            TEST_DATA_FOLDER.resolve("studentIdTooLargeAddressBook.json");
    private static final Path OLD_FORMAT_FILE = TEST_DATA_FOLDER.resolve("oldFormatAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
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
    public void toModelType_duplicateStudentIds_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_ID_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class,
                String.format(JsonSerializableAddressBook.MESSAGE_DUPLICATE_STUDENT_ID, 1),
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_missingNextStudentId_usesHighestPlusOne() throws Exception {
        assertEquals(4, readNextStudentId(null));
    }

    @Test
    public void toModelType_nextStudentIdNotAboveHighest_usesHighestPlusOne() throws Exception {
        assertEquals(4, readNextStudentId("3"));
    }

    @Test
    public void toModelType_invalidNextStudentId_usesHighestPlusOne() throws Exception {
        assertEquals(4, readNextStudentId("abc"));
    }

    @Test
    public void toModelType_nextStudentIdAboveHighest_keepsSavedValue() throws Exception {
        assertEquals(10, readNextStudentId("10"));
    }

    @Test
    public void toModelType_studentIdTooLarge_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(STUDENT_ID_TOO_LARGE_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, StudentId.MESSAGE_CONSTRAINTS, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nextStudentIdTooLarge_usesHighestPlusOne() throws Exception {
        assertEquals(4, readNextStudentId("2147483647"));
    }

    @Test
    public void toModelType_nextStudentIdNoneLeft_keepsSavedValue() throws Exception {
        assertEquals(AddressBook.NEXT_STUDENT_ID_NONE_LEFT,
                readNextStudentId(String.valueOf(AddressBook.NEXT_STUDENT_ID_NONE_LEFT)));
    }

    @Test
    public void toModelType_oldFormatFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(OLD_FORMAT_FILE,
                JsonSerializableAddressBook.class).get();
        String expectedMessage = String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                StudentId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dataFromFile::toModelType);
    }

    /**
     * Returns the next student ID of an address book holding ALICE (student ID 1) and CARL (student ID 3),
     * loaded with {@code savedNextStudentId} as the saved next student ID.
     */
    private static int readNextStudentId(String savedNextStudentId) throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedPerson(ALICE), new JsonAdaptedPerson(CARL)), savedNextStudentId);
        return data.toModelType().getNextStudentId();
    }

}
