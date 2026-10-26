package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.Address;
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
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_SCHOOL = "ABC Secondary School";
    public static final String DEFAULT_ADDRESS = "Not provided";
    public static final String DEFAULT_LEVEL = "S3";

    private Name name;
    private Phone phone;
    private School school;
    private Address address;
    private Level level;
    private List<Subject> subjects;
    private List<Note> notes;
    private Guardian guardian;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        school = new School(DEFAULT_SCHOOL);
        address = new Address(DEFAULT_ADDRESS);
        level = Level.fromString(DEFAULT_LEVEL);
        subjects = List.of(new Subject("Mathematics", Band.G1));
        notes = new ArrayList<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone().orElse(null);
        school = personToCopy.getSchool();
        address = personToCopy.getAddress().orElse(null);
        level = personToCopy.getLevel();
        subjects = personToCopy.getSubjects();
        notes = new ArrayList<>(personToCopy.getNotes());
        guardian = personToCopy.getGuardian().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Removes the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withoutPhone() {
        this.phone = null;
        return this;
    }

    /**
     * Sets the {@code School} of the {@code Person} that we are building.
     */
    public PersonBuilder withSchool(String school) {
        this.school = new School(school);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Removes the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withoutAddress() {
        this.address = null;
        return this;
    }

    /**
     * Parses the {@code notes} into a {@code List<Note>} and sets it to the {@code Person} being built.
     */
    public PersonBuilder withNotes(String ... notes) {
        this.notes = Arrays.stream(notes)
                .map(Note::new)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the {@code Level} of the {@code Person} that we are building.
     */
    public PersonBuilder withLevel(String level) {
        this.level = Level.fromString(level);
        return this;
    }

    /**
     * Sets the subjects of the person being built.
     */
    public PersonBuilder withSubjects(Subject... subjects) {
        this.subjects = List.of(subjects);
        return this;
    }

    /**
     * Sets the guardian of the {@code Person} that we are building.
     */
    public PersonBuilder withGuardian(String guardianName, String guardianPhone) {
        this.guardian = new Guardian(new Name(guardianName), new Phone(guardianPhone));
        return this;
    }

    /**
     * Removes the guardian of the {@code Person} that we are building.
     */
    public PersonBuilder withoutGuardian() {
        this.guardian = null;
        return this;
    }

    public Person build() {
        return new Person(name, phone, school, address, level, subjects, notes, guardian);
    }

}
