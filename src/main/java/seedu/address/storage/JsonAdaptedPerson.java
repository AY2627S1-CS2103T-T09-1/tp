package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.person.Subject;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String NULL_NOTES_MESSAGE = "Person's notes field cannot be null!";
    public static final String NULL_NOTE_MESSAGE = "Person's notes field cannot contain a null note!";

    private final String name;
    // Left out of the file when the person has no phone
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String phone;
    private final String school;
    private final String level;
    private final List<JsonAdaptedSubject> subjects = new ArrayList<>();
    // Set by Jackson directly, so files without notes still load
    private final List<JsonAdaptedNote> notes = new ArrayList<>();
    // Set by Jackson directly, so files without a guardian still load.
    // Left out of the file when the person has no guardian.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonAdaptedGuardian guardian;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("school") String school, @JsonProperty("level") String level,
            @JsonProperty("subjects") List<JsonAdaptedSubject> subjects) {
        this.name = name;
        this.phone = phone;
        this.school = school == null ? School.DEFAULT_VALUE : school;
        this.level = level;
        if (subjects != null) {
            this.subjects.addAll(subjects);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().map(p -> p.value).orElse(null);
        school = source.getSchool().value;
        level = source.getLevel().name();
        subjects.addAll(source.getSubjects().stream().map(JsonAdaptedSubject::new).toList());
        notes.addAll(source.getNotes().stream()
            .map(JsonAdaptedNote::new).collect(Collectors.toList()));
        guardian = source.getGuardian().map(JsonAdaptedGuardian::new).orElse(null);
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        // Phone is optional, so a missing phone means the person has no phone.
        // It is cleaned like a typed phone, so a hand-edited file can use +65, spaces, and hyphens.
        final String normalizedPhone = phone == null ? null : Phone.normalize(phone);
        if (normalizedPhone != null && !Phone.isValidPhone(normalizedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = normalizedPhone == null ? null : new Phone(normalizedPhone);

        if (!School.isValidSchool(school)) {
            throw new IllegalValueException(School.MESSAGE_CONSTRAINTS);
        }
        final School modelSchool = new School(school);

        if (level == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName()));
        }
        final Level modelLevel;
        try {
            modelLevel = Level.fromString(level);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(Level.MESSAGE_CONSTRAINTS);
        }

        final List<Subject> modelSubjects = new ArrayList<>();
        for (JsonAdaptedSubject subject : subjects) {
            if (subject == null) {
                throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
            }
            modelSubjects.add(subject.toModelType());
        }

        final List<Note> modelNotes = toModelNotes();
        // Guardian is optional, so a missing guardian means the person has no guardian
        final Guardian modelGuardian = guardian == null ? null : guardian.toModelType();
        try {
            return new Person(modelName, modelPhone, modelSchool, modelLevel, modelSubjects, modelNotes,
                    modelGuardian);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage(), e);
        }
    }

    /**
     * Converts the adapted notes into the model's {@code Note} objects.
     *
     * @throws IllegalValueException if the notes field is null, or contains a null or invalid note.
     */
    private List<Note> toModelNotes() throws IllegalValueException {
        // Jackson replaces the default empty list with null when the file has "notes": null
        if (notes == null) {
            throw new IllegalValueException(NULL_NOTES_MESSAGE);
        }

        final List<Note> modelNotes = new ArrayList<>();
        for (JsonAdaptedNote note : notes) {
            // Jackson adds a null entry for each null in the list without calling JsonAdaptedNote
            if (note == null) {
                throw new IllegalValueException(NULL_NOTE_MESSAGE);
            }
            modelNotes.add(note.toModelType());
        }
        return modelNotes;
    }

}
