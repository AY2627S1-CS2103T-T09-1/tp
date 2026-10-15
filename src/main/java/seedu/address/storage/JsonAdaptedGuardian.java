package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Jackson-friendly version of {@link Guardian}.
 */
class JsonAdaptedGuardian {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Guardian's %s field is missing!";

    private final String name;
    private final String phone;

    /**
     * Constructs a {@code JsonAdaptedGuardian} with the given guardian details.
     */
    @JsonCreator
    public JsonAdaptedGuardian(@JsonProperty("name") String name, @JsonProperty("phone") String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Converts a given {@code Guardian} into this class for Jackson use.
     */
    public JsonAdaptedGuardian(Guardian source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
    }

    /**
     * Converts this Jackson-friendly adapted guardian object into the model's {@code Guardian} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted guardian.
     */
    public Guardian toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Guardian.MESSAGE_NAME_CONSTRAINTS);
        }
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Guardian.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Guardian(new Name(name), new Phone(phone));
    }

}
