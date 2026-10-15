package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Band;
import seedu.address.model.person.Subject;

/**
 * Stores a subject name and band in a Jackson-friendly form.
 */
class JsonAdaptedSubject {

    private final String name;
    private final String band;

    /**
     * Constructs an adapted subject from its stored fields.
     */
    @JsonCreator
    public JsonAdaptedSubject(@JsonProperty("name") String name, @JsonProperty("band") String band) {
        this.name = name;
        this.band = band;
    }

    /**
     * Converts a subject to its storage representation.
     */
    public JsonAdaptedSubject(Subject source) {
        name = source.getName();
        band = source.getBand().name();
    }

    /**
     * Returns the validated subject represented by the stored fields.
     */
    public Subject toModelType() throws IllegalValueException {
        if (name == null || !Subject.isValidSubject(name)) {
            throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
        }
        if (band == null) {
            throw new IllegalValueException(Band.MESSAGE_CONSTRAINTS);
        }
        try {
            return new Subject(name, Band.fromString(band));
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage(), e);
        }
    }
}
