package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final int MAX_SUBJECTS = 8;
    public static final String MESSAGE_MISSING_SUBJECT = "A student must have at least one subject.";
    public static final String MESSAGE_TOO_MANY_SUBJECTS = "A student may have at most 8 subjects.";
    public static final String MESSAGE_REPEATED_SUBJECT = "Subject is repeated: %s.";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final School school;
    private final List<Subject> subjects;
    private final Set<Tag> tags = new HashSet<>();
    private final List<String> notes = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, School school,
            List<Subject> subjects, Set<Tag> tags) {
        this(name, phone, email, address, school, subjects, tags, List.of());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, School school,
            List<Subject> subjects, Set<Tag> tags, List<String> notes) {
        requireAllNonNull(name, phone, email, address, school, subjects, tags, notes);
        List<Subject> validatedSubjects = List.copyOf(subjects);
        validateSubjects(validatedSubjects);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.school = school;
        this.subjects = validatedSubjects;
        this.tags.addAll(tags);
        this.notes.addAll(notes);
    }

    private static void validateSubjects(List<Subject> subjects) {
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

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public School getSchool() {
        return school;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
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
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && school.equals(otherPerson.school)
                && subjects.equals(otherPerson.subjects)
                && tags.equals(otherPerson.tags)
                && notes.equals(otherPerson.notes);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, school, subjects, tags, notes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("school", school)
                .add("subjects", subjects)
                .add("tags", tags)
                .toString();
    }

}
