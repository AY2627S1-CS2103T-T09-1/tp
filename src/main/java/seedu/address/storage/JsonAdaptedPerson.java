package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String NULL_NOTES_MESSAGE = "Person's notes field cannot be null!";
    public static final String NULL_NOTE_MESSAGE = "Person's notes field cannot contain a null note!";

    private final String name;
    private final String phone;
    private final String school;
    // Set by Jackson directly, so files without notes still load
    private final List<JsonAdaptedNote> notes = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("school") String school) {
        this.name = name;
        this.phone = phone;
        this.school = school == null ? School.DEFAULT_VALUE : school;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        school = source.getSchool().value;
        notes.addAll(source.getNotes().stream()
                .map(JsonAdaptedNote::new)
                .collect(Collectors.toList()));
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

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (!School.isValidSchool(school)) {
            throw new IllegalValueException(School.MESSAGE_CONSTRAINTS);
        }
        final School modelSchool = new School(school);

        final List<Note> modelNotes = toModelNotes();
        return new Person(modelName, modelPhone, modelSchool, modelNotes);
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
