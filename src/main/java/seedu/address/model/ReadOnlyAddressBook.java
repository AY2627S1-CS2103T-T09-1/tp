package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns the student ID that the next added student will get.
     * It is always greater than every student ID in the persons list.
     * A value above {@code StudentId.MAX_VALUE} means no student IDs are left.
     */
    int getNextStudentId();

}
