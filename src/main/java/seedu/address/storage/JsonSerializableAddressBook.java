package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_STUDENT_ID = "Persons list contains more than one person "
            + "with student ID %1$s.";

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final String nextStudentId;

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and next student ID.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("nextStudentId") String nextStudentId) {
        this.persons.addAll(persons);
        this.nextStudentId = nextStudentId;
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        nextStudentId = source.getNextStudentId().toString();
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        Set<StudentId> studentIds = new HashSet<>();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            if (!studentIds.add(person.getStudentId())) {
                throw new IllegalValueException(String.format(MESSAGE_DUPLICATE_STUDENT_ID, person.getStudentId()));
            }
            addressBook.addPerson(person);
        }
        restoreNextStudentId(addressBook);
        return addressBook;
    }

    /**
     * Sets the next student ID of {@code addressBook} to the saved value if it is valid.
     * {@code addressBook} must already hold all the persons, so its next student ID is
     * the highest student ID + 1. A saved value that is missing, invalid, or not above
     * the highest student ID is ignored with a warning.
     */
    private void restoreNextStudentId(AddressBook addressBook) {
        StudentId lowestAllowed = addressBook.getNextStudentId();
        if (nextStudentId == null || !StudentId.isValidStudentId(nextStudentId)) {
            logger.warning("Next student ID \"" + nextStudentId + "\" is missing or invalid. Using "
                    + lowestAllowed + " instead.");
            return;
        }

        StudentId savedNextStudentId = new StudentId(Integer.parseInt(nextStudentId));
        if (savedNextStudentId.value < lowestAllowed.value) {
            logger.warning("Next student ID " + savedNextStudentId + " is not above the highest student ID. Using "
                    + lowestAllowed + " instead.");
            return;
        }
        addressBook.raiseNextStudentId(savedNextStudentId);
    }

}
