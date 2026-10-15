package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
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

    public static final int MAX_SUBJECTS = 8;
    public static final String MESSAGE_MISSING_SUBJECT = "A student must have at least one subject.";
    public static final String MESSAGE_TOO_MANY_SUBJECTS = "A student may have at most 8 subjects.";
    public static final String MESSAGE_REPEATED_SUBJECT = "Subject is repeated: %s.";

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final School school;
    private final Level level;
    private final List<Subject> subjects;
    private final List<String> notes = new ArrayList<>();

    /**
     * Every field except {@code phone} must be present and not null.
     * A null {@code phone} means the person has no phone number.
     */
    public Person(Name name, Phone phone, School school, Level level, List<Subject> subjects) {
        this(name, phone, school, level, subjects, List.of());
    }

    /**
     * Every field except {@code phone} must be present and not null.
     * A null {@code phone} means the person has no phone number.
     */
    public Person(Name name, Phone phone, School school, Level level, List<Subject> subjects, List<String> notes) {
        requireAllNonNull(name, school, level, subjects, notes);
        List<Subject> validatedSubjects = List.copyOf(subjects);
        validateSubjects(validatedSubjects);
        this.name = name;
        this.phone = phone;
        this.school = school;
        this.level = level;
        this.subjects = validatedSubjects;
        this.notes.addAll(notes);
    }

    /**
     * Validates the subject count and rejects repeated subject names, regardless of band.
     */
    public static void validateSubjects(List<Subject> subjects) {
        requireAllNonNull(subjects);
        checkArgument(!subjects.isEmpty(), MESSAGE_MISSING_SUBJECT);
        checkArgument(subjects.size() <= MAX_SUBJECTS, MESSAGE_TOO_MANY_SUBJECTS);
        List<Subject> uniqueSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            boolean isRepeated = uniqueSubjects.stream().anyMatch(subject::hasSameName);
            checkArgument(!isRepeated, String.format(MESSAGE_REPEATED_SUBJECT, subject.getName()));
            uniqueSubjects.add(subject);
        }
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public List<String> getNotes() {
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
                && subjects.equals(otherPerson.subjects)
                && notes.equals(otherPerson.notes);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, school, level, subjects, notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("school", school)
                .add("level", level)
                .add("subjects", subjects)
                .toString();
    }

}
