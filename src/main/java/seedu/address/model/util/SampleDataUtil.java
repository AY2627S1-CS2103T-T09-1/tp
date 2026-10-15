package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    private SampleDataUtil() {}

    /**
     * Returns the sample persons with their contact details and schools.
     */
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                    new School("Hwa Chong Institution"), Level.S3),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                    new School("Nanyang Girls’ High School"), Level.S2),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new School("National Junior College"), Level.S4),
            new Person(new Name("David Li"), new Phone("91031282"),
                    new School("River Valley High School"), Level.S1),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                    new School("Anglo-Chinese School"), Level.S5),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                    new School("Victoria School"), Level.S3)
        };
    }

    /**
     * Returns an address book containing the sample persons.
     */
    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
