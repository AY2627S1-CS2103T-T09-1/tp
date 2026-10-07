package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSamePerson comparison).
 * Student IDs are never reused: the next student ID only goes up, even when persons are removed.
 */
public class AddressBook implements ReadOnlyAddressBook {

    /** The next student ID once every student ID has been used. No more persons can be added. */
    public static final int NEXT_STUDENT_ID_NONE_LEFT = StudentId.MAX_VALUE + 1;
    public static final String MESSAGE_NEXT_STUDENT_ID_CONSTRAINTS = "The next student ID should be from "
            + StudentId.FIRST_VALUE + " to " + NEXT_STUDENT_ID_NONE_LEFT;

    private final UniquePersonList persons = new UniquePersonList();
    private int nextStudentId = StudentId.FIRST_VALUE;

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData},
     * including its next student ID.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        nextStudentId = newData.getNextStudentId();
    }

    /**
     * Returns true if a given number is a valid next student ID.
     * {@link #NEXT_STUDENT_ID_NONE_LEFT} is valid and means no student IDs are left.
     */
    public static boolean isValidNextStudentId(int test) {
        return test >= StudentId.FIRST_VALUE && test <= NEXT_STUDENT_ID_NONE_LEFT;
    }

    /**
     * Raises the next student ID to {@code newNextStudentId} if it is higher.
     * The next student ID never goes down, so student IDs are never reused.
     */
    public void raiseNextStudentId(int newNextStudentId) {
        checkArgument(isValidNextStudentId(newNextStudentId), MESSAGE_NEXT_STUDENT_ID_CONSTRAINTS);
        nextStudentId = Math.max(nextStudentId, newNextStudentId);
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     * The next student ID is raised above the person's student ID if needed.
     */
    public void addPerson(Person p) {
        // Worked out before the person is added, so a failure cannot leave the data half-updated
        int newNextStudentId = Math.max(nextStudentId, p.getStudentId().value + 1);
        persons.add(p);
        nextStudentId = newNextStudentId;
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("nextStudentId", nextStudentId)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public int getNextStudentId() {
        return nextStudentId;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        return persons.equals(otherAddressBook.persons)
                && nextStudentId == otherAddressBook.nextStudentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, nextStudentId);
    }
}
