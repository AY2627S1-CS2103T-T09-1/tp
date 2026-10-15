package seedu.address.model.util;

import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Band;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    private SampleDataUtil() {}

    /**
     * Returns fictional sample students with contact details, subjects, bands, guardians, and tutoring notes.
     */
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Chloe Tan"), new Phone("91234567"),
                    new School("Bedok Green Secondary School"), Level.S2,
                    List.of(new Subject("Mathematics", Band.G1), new Subject("Science", Band.G2)),
                    List.of(new Note("Needs more practice with algebra.")),
                    new Guardian(new Name("Amy Tan"), new Phone("92345678"))),
            new Person(new Name("Marcus Lim"), new Phone("82345678"),
                    new School("Tampines Secondary School"), Level.S5,
                    List.of(new Subject("English", Band.G2), new Subject("Mathematics", Band.G2)),
                    List.of(new Note("Review comprehension techniques before the next lesson.")),
                    new Guardian(new Name("Grace Lim"), new Phone("98765432"))),
            new Person(new Name("Aisha Rahman"), new Phone("93456789"),
                    new School("Pasir Ris Secondary School"), Level.S3,
                    List.of(new Subject("Chemistry", Band.G3), new Subject("Mathematics", Band.G3)),
                    List.of(new Note("Strong in calculations; revise chemical bonding."))),
            new Person(new Name("Daniel Lee"), null,
                    new School("Victoria School"), Level.S1,
                    List.of(new Subject("Mathematics", Band.G2), new Subject("Science", Band.G2)),
                    List.of(new Note("Practise fractions before the next lesson."))),
            new Person(new Name("Natalie Goh"), new Phone("84567890"),
                    new School("Cedar Girls' Secondary School"), Level.S4,
                    List.of(new Subject("Physics", Band.G3), new Subject("Chemistry", Band.G3)),
                    List.of(new Note("WA2 Physics: 36/50. Focus on electricity.")),
                    new Guardian(new Name("Michelle Goh"), new Phone("81234567"))),
            new Person(new Name("Ethan Kumar"), new Phone("95678901"),
                    new School("Springfield Secondary School"), Level.S2,
                    List.of(new Subject("Geography", Band.G1), new Subject("English", Band.G2)),
                    List.of(new Note("Bring the completed map-reading worksheet.")))
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
