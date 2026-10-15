package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details except phone are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final School school;
    private final Level level;
    private final List<Note> notes = new ArrayList<>();

    /**
     * Every field except {@code phone} must be present and not null.
     * A null {@code phone} means the person has no phone number.
     */
    public Person(Name name, Phone phone, School school, Level level) {
        this(name, phone, school, level, List.of());
    }

    /**
     * Every field except {@code phone} must be present and not null.
     * A null {@code phone} means the person has no phone number.
     */
    public Person(Name name, Phone phone, School school, Level level, List<Note> notes) {
        requireAllNonNull(name, school, level, notes);
        this.name = name;
        this.phone = phone;
        this.school = school;
        this.level = level;
        this.notes.addAll(notes);
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    public School getSchool() {
        return school;
    }

    public Level getLevel() {
        return level;
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && school.equals(otherPerson.school)
                && level.equals(otherPerson.level)
                && notes.equals(otherPerson.notes);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, school, level, notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("school", school)
                .add("level", level)
                .toString();
    }

}
